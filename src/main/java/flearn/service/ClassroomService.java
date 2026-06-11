package flearn.service;

import flearn.dto.request.AssignTeacherRequest;
import flearn.dto.request.ClassroomRequest;

import flearn.dto.response.AdminStatisticsResponse;
import flearn.dto.response.ClassroomResponse;
import flearn.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface ClassroomService {
    AdminStatisticsResponse getAdminStatistics();

    List<ClassroomResponse> getAllClasses();

    List<ClassroomResponse> searchAllClasses(String keyword);

    List<ClassroomResponse> getClassesByTeacher(User teacher);

    ClassroomResponse getClassById(Integer classId);

    void createClass(@Valid ClassroomRequest request);

    void updateClass(Integer classId, @Valid ClassroomRequest request);

    ClassroomResponse getTeacherClassById(Integer classId, User teacher);

    void toggleClassStatus(Integer classId);

    void toggleTeacherClassStatus(Integer classId, User teacher);

    void softDeleteClass(Integer classId, User teacher);

    void assignTeacher(Integer classId, @Valid AssignTeacherRequest request);

}
