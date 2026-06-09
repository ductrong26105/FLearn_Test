package flearn.service;

import flearn.entity.User;
import flearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    // Bước 1: Xử lý khi người dùng nhập Email yêu cầu cấp lại pass
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này trong hệ thống."));

        // Tạo mã Token ngẫu nhiên (UUID)
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);

        // Cài đặt thời gian hết hạn là 15 phút tính từ hiện tại
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 15);
        user.setResetTokenExpiry(cal.getTime());

        userRepository.save(user);

        // Chuẩn bị nội dung Email và Gửi đi
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        String emailText = "Xin chào " + user.getFullName() + ",\n\n"
                + "Bạn đã yêu cầu đặt lại mật khẩu cho hệ thống ACE TEAM LMS. Vui lòng click vào đường dẫn dưới đây để đổi mật khẩu mới:\n"
                + resetLink + "\n\n"
                + "Lưu ý: Đường dẫn này sẽ tự động hết hạn sau 15 phút.\n"
                + "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.";

        emailService.sendEmail(user.getEmail(), "ACE TEAM - Yêu cầu đặt lại mật khẩu", emailText);
    }

    // Bước 2: Kiểm tra Token khi người dùng bấm vào link trong thư
    public User getByResetToken(String token) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Đường dẫn không hợp lệ hoặc không tồn tại."));

        if (user.getResetTokenExpiry().before(new Date())) {
            throw new RuntimeException("Đường dẫn này đã hết hạn. Vui lòng yêu cầu cấp lại mã mới.");
        }
        return user;
    }

    // Bước 3: Cập nhật mật khẩu mới
    public void updatePassword(User user, String newPassword) {
        // Lưu ý: Đang lưu mật khẩu KHÔNG MÃ HÓA theo yêu cầu trước đó của bạn
        user.setPasswordHash(newPassword);

        // Cập nhật xong thì xóa Token đi để không bị dùng lại lần nữa
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}