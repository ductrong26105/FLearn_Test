package flearn.service;

import flearn.dto.request.ChangePasswordRequest;
import flearn.dto.request.RegisterStudentRequest;
import flearn.entity.User;
import jakarta.validation.Valid;

public interface AuthService {
    void registerStudent(@Valid RegisterStudentRequest request);

    void changePassword(User user, @Valid ChangePasswordRequest request);
}
