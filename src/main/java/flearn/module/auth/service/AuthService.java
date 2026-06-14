package flearn.module.auth.service;

import flearn.module.auth.dto.request.ChangePasswordRequest;
import flearn.module.auth.dto.request.RegisterStudentRequest;
import flearn.entity.User;
import jakarta.validation.Valid;

public interface AuthService {
    void registerStudent(@Valid RegisterStudentRequest request);

    void changePassword(User user, @Valid ChangePasswordRequest request);
}
