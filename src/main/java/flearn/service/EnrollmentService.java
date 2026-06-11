package flearn.service;

import flearn.dto.request.RejectEnrollmentRequest;
import flearn.dto.response.EnrollmentResponse;
import flearn.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentResponse> getClassEnrollments(Integer classId, User teacher);

    List<EnrollmentResponse> searchActiveStudentsInClass(Integer classId, User teacher, String keyword);

    void approveEnrollment(Integer classId, Integer enrollmentId, User teacher);

    void rejectEnrollment(Integer classId, Integer enrollmentId, User teacher, @Valid RejectEnrollmentRequest request);

    void removeStudent(Integer classId, Integer studentId, User teacher);
}
