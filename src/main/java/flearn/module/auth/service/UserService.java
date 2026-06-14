package flearn.module.auth.service;

import flearn.module.management.dto.request.CreateTeacherRequest;
import flearn.module.auth.dto.request.UpdateUserRequest;
import flearn.module.auth.dto.response.UserResponse;
import flearn.enums.Role;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersByRole(Role role);

    List<UserResponse> searchUsersByRole(Role role, String keyword);

    UserResponse getUserById(Integer userId);

    void createTeacher(@Valid CreateTeacherRequest request);

    void updateUser(Integer userId, @Valid UpdateUserRequest request);

    void toggleUserStatus(Integer userId);

    void blockUser(Integer userId);

    void unblockUser(Integer userId);
}
