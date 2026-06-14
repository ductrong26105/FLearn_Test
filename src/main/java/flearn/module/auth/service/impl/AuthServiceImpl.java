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

    @Override
    public void registerStudent(RegisterStudentRequest request) {
        validateUniqueAccount(request.getUsername(), request.getEmail());

        User newUser = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(Role.STUDENT.getCode())
                .status(UserStatus.ACTIVE)
                .isActive(true)
                .build();
        userRepository.save(newUser);
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
