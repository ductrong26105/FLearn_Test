package flearn.module.auth.service.impl;

import flearn.entity.User;
import flearn.common.exception.BusinessException;
import flearn.repository.UserRepository;
import flearn.common.service.EmailService;
import flearn.module.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final int OTP_EXPIRY_MINUTES = 10;
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 15;
    private static final String RESET_PASSWORD_URL = "http://localhost:8080/reset-password?token=";

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // OTP FLOW
    // ==========================================

    @Override
    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new BusinessException("Không tìm thấy tài khoản với email này trong hệ thống."));

        if (user.isBlocked()) {
            throw new BusinessException("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên.");
        }

        // Sinh OTP 6 chữ số
        String otp = generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(createExpiry(OTP_EXPIRY_MINUTES));
        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "FLearn - Mã OTP đặt lại mật khẩu",
                buildOtpEmail(user.getFullName(), otp)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User verifyOtp(String email, String otpCode) {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new BusinessException("Email không hợp lệ."));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otpCode.trim())) {
            throw new BusinessException("Mã OTP không đúng. Vui lòng kiểm tra lại.");
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().before(new Date())) {
            throw new BusinessException("Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
        }
        return user;
    }

    @Override
    public void resetPasswordWithOtp(String email, String otpCode, String newPassword) {
        // Inline verify để tránh vấn đề Spring AOP self-invocation
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new BusinessException("Email không hợp lệ."));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otpCode.trim())) {
            throw new BusinessException("Mã OTP không đúng. Vui lòng kiểm tra lại.");
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().before(new Date())) {
            throw new BusinessException("Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        // Xóa OTP sau khi dùng
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }

    // ==========================================
    // TOKEN FLOW (giữ để tương thích ngược)
    // ==========================================

    @Override
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Không tìm thấy tài khoản với email này trong hệ thống."));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(createExpiry(RESET_TOKEN_EXPIRY_MINUTES));
        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "FLearn - Yêu cầu đặt lại mật khẩu",
                buildResetPasswordEmail(user.getFullName(), token)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User getByResetToken(String token) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new BusinessException("Đường dẫn không hợp lệ hoặc không tồn tại."));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().before(new Date())) {
            throw new BusinessException("Đường dẫn này đã hết hạn. Vui lòng yêu cầu cấp lại mã mới.");
        }
        return user;
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    // ==========================================
    // PRIVATE HELPERS
    // ==========================================

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6 chữ số
        return String.valueOf(otp);
    }

    private Date createExpiry(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private String buildOtpEmail(String fullName, String otp) {
        return "Xin chào " + fullName + ",\n\n"
                + "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản FLearn.\n\n"
                + "Mã OTP của bạn là:\n\n"
                + "    " + otp + "\n\n"
                + "Mã này có hiệu lực trong " + OTP_EXPIRY_MINUTES + " phút.\n\n"
                + "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n"
                + "Trân trọng,\nĐội ngũ FLearn";
    }

    private String buildResetPasswordEmail(String fullName, String token) {
        return "Xin chào " + fullName + ",\n\n"
                + "Bạn đã yêu cầu đặt lại mật khẩu cho hệ thống FLearn.\n"
                + "Vui lòng click vào đường dẫn dưới đây để đổi mật khẩu mới:\n"
                + RESET_PASSWORD_URL + token + "\n\n"
                + "Lưu ý: Đường dẫn này sẽ tự động hết hạn sau " + RESET_TOKEN_EXPIRY_MINUTES + " phút.\n"
                + "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.";
    }
}
