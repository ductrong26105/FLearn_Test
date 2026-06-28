package flearn.module.auth.service.impl;

import flearn.module.auth.dto.request.ChangePasswordRequest;
import flearn.module.auth.dto.request.RegisterStudentRequest;
import flearn.entity.User;
import flearn.enums.Role;
import flearn.enums.UserStatus;
import flearn.common.exception.BusinessException;
import flearn.repository.UserRepository;
import flearn.module.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final flearn.common.service.EmailService emailService;
    private final org.thymeleaf.TemplateEngine templateEngine;

    @Override
    public void registerStudent(RegisterStudentRequest request) {
        validateUniqueAccount(request.getUsername(), request.getEmail());

        String otp = generateOtp();

        User newUser = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(Role.STUDENT.getCode())
                .status(UserStatus.INACTIVE) // Lưu tài khoản ở trạng thái INACTIVE
                .isActive(false)
                .otpCode(otp)
                .otpExpiry(createExpiry(10)) // OTP có hạn 10 phút
                .build();
        userRepository.save(newUser);

        // Gửi email OTP
        org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
        context.setVariable("userName", newUser.getFullName());
        context.setVariable("emailAddress", newUser.getEmail());
        context.setVariable("otpCode", otp);
        context.setVariable("expiryMinutes", 10);
        context.setVariable("actionType", "Mã Xác Thực Tạo Tài Khoản");
        context.setVariable("actionTypeLowerCase", "xác thực tạo tài khoản");

        String htmlContent = templateEngine.process("email/otp-email", context);
        emailService.sendHtmlEmail(newUser.getEmail(), "FLearn - Mã Xác Thực Tài Khoản", htmlContent);
    }

    @Override
    public void verifyAccount(String email, String otpCode) {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new BusinessException("Tài khoản không tồn tại."));

        if (user.getStatus() != UserStatus.INACTIVE) {
            throw new BusinessException("Tài khoản đã được xác thực trước đó.");
        }
        if (user.getOtpCode() == null || !user.getOtpCode().equals(otpCode.trim())) {
            throw new BusinessException("Mã OTP không đúng. Vui lòng kiểm tra lại.");
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().before(new java.util.Date())) {
            throw new BusinessException("Mã OTP đã hết hạn. Vui lòng đăng ký lại để nhận mã mới.");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setIsActive(true);
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }

    private String generateOtp() {
        java.security.SecureRandom random = new java.security.SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private java.util.Date createExpiry(int minutes) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    @Override
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BusinessException("Mật khẩu hiện tại không đúng.");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Xác nhận mật khẩu mới không khớp.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private void validateUniqueAccount(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException("Tên đăng nhập đã tồn tại.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email này đã được sử dụng.");
        }
    }
}
