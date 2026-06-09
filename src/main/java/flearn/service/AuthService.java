package flearn.service;

import flearn.entity.User;
import flearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService; // Gọi bưu tá giao mail vào đây

    public void registerStudent(String username, String password, String fullName, String email) {
        // 1. Kiểm tra định dạng Email bằng Regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            throw new RuntimeException("Email không đúng định dạng! (VD: name@domain.com)");
        }

        // 2. Kiểm tra trùng lặp
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email này đã được sử dụng!");
        }

        // 3. Tạo mã kích hoạt ngẫu nhiên
        String activationToken = UUID.randomUUID().toString();

        // 4. Lưu tài khoản với trạng thái khóa (isActive = false)
        User newUser = User.builder()
                .username(username)
                .passwordHash(password) // Vẫn đang lưu pass không mã hóa
                .fullName(fullName)
                .email(email)
                .role(2)
                .isActive(false) // KHÓA TÀI KHOẢN KHI CHƯA XÁC THỰC
                .resetToken(activationToken) // Mượn tạm cột này lưu mã kích hoạt
                .build();
        userRepository.save(newUser);

        // 5. Gửi Email xác thực
        String verifyLink = "http://localhost:8080/verify-account?token=" + activationToken;
        String emailText = "Chào " + fullName + ",\n\n"
                + "Cảm ơn bạn đã đăng ký tài khoản trên hệ thống FLearn.\n"
                + "Vui lòng click vào đường dẫn dưới đây để xác thực email và kích hoạt tài khoản của bạn:\n"
                + verifyLink + "\n\n"
                + "Nếu bạn không đăng ký tài khoản này, vui lòng bỏ qua email.";

        emailService.sendEmail(email, "ACE TEAM - Xác thực tài khoản đăng ký", emailText);
    }

    // Hàm xử lý khi người dùng click vào Link trong Email
    public void verifyAccount(String token) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Mã xác thực không hợp lệ hoặc tài khoản đã được kích hoạt!"));

        user.setIsActive(true);      // Mở khóa tài khoản
        user.setResetToken(null);    // Xóa mã kích hoạt đi để không dùng lại được nữa
        userRepository.save(user);
    }
}