package flearn.module.management.service;

import flearn.module.management.dto.request.JoinClassRequest;
import flearn.module.management.dto.response.EnrollmentResponse;
import flearn.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface StudentService {
    List<EnrollmentResponse> getJoinedClasses(User student);

    void joinClass(@Valid JoinClassRequest request, User student);
}
