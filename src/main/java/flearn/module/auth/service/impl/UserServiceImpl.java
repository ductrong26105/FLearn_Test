package flearn.module.auth.service.impl;

import flearn.module.management.dto.request.CreateTeacherRequest;
import flearn.module.auth.dto.request.UpdateUserRequest;
import flearn.module.auth.dto.response.UserResponse;
import flearn.enums.Role;
import flearn.entity.User;
import flearn.enums.UserStatus;
import flearn.common.exception.BusinessException;
import flearn.module.auth.mapper.UserMapper;
import flearn.repository.UserRepository;
import flearn.module.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    @Override
    public List<UserResponse> getUsersByRole(Role role) {
        return userMapper.toResponseList(userRepository.findByRole(role.getCode()));
    }

    @Override
    public List<UserResponse> searchUsersByRole(Role role, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getUsersByRole(role);
        }
        return userMapper.toResponseList(userRepository.searchByRole(role.getCode(), keyword.trim()));
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        return userMapper.toResponse(findUserById(userId));
    }

    @Override
    @Transactional
    public void createTeacher(CreateTeacherRequest request) {
        validateUniqueAccount(request.getUsername(), request.getEmail(), null);
        User teacher = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .role(Role.TEACHER.getCode())
                .status(UserStatus.ACTIVE)
                .isActive(true)
                .build();
        userRepository.save(teacher);
    }

    @Override
    @Transactional
    public void updateUser(Integer userId, UpdateUserRequest request) {
        User user = findUserById(userId);
        validateUniqueAccount(user.getUsername(), request.getEmail(), userId);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDepartment(request.getDepartment());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void toggleUserStatus(Integer userId) {
        User user = findUserById(userId);
        if (user.getStatus() == UserStatus.BLOCKED) {
            activate(user);
        } else {
            block(user);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void blockUser(Integer userId) {
        User user = findUserById(userId);
        block(user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unblockUser(Integer userId) {
        User user = findUserById(userId);
        activate(user);
        userRepository.save(user);
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy người dùng."));
    }

    private void validateUniqueAccount(String username, String email, Integer currentUserId) {
        userRepository.findByUsername(username).ifPresent(existing -> {
            if (!existing.getUserId().equals(currentUserId)) {
                throw new BusinessException("Tên đăng nhập đã tồn tại.");
            }
        });
        userRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getUserId().equals(currentUserId)) {
                throw new BusinessException("Email đã tồn tại.");
            }
        });
    }

    private void block(User user) {
        user.setStatus(UserStatus.BLOCKED);
        user.setIsActive(false);
    }

    private void activate(User user) {
        user.setStatus(UserStatus.ACTIVE);
        user.setIsActive(true);
    }
}
