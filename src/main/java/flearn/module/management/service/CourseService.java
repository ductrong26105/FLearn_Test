package flearn.module.management.service;

import flearn.module.management.dto.request.CourseRequest;
import flearn.module.management.dto.response.CourseResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {
    List<CourseResponse> getAllCourses();
    List<CourseResponse> searchCourses(String keyword);
    CourseResponse getCourseById(Integer id);
    void createCourse(@Valid CourseRequest request);
    void updateCourse(Integer id, @Valid CourseRequest request);
    void toggleCourseStatus(Integer id);
    void deleteCourse(Integer id);
}
