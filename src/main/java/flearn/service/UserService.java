package flearn.service;

import flearn.entity.User;
import flearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Lấy toàn bộ danh sách user cho Admin
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Admin tạo tài khoản cho Giáo viên
    public void createTeacher(String username, String password, String fullName, String email) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new RuntimeException("Tên đăng nhập hoặc Email đã tồn tại!");
        }
        User teacher = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .fullName(fullName)
                .email(email)
                .role(1) // 1 là Role Giáo viên
                .isActive(true)
                .build();
        userRepository.save(teacher);
    }

    // Khóa / Mở khóa tài khoản
    public void toggleUserStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        user.setIsActive(!user.getIsActive()); // Đảo ngược trạng thái
        userRepository.save(user);
    }
}