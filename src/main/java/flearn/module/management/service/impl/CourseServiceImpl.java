package flearn.module.management.service.impl;

import flearn.module.management.dto.request.CourseRequest;
import flearn.module.management.dto.response.CourseResponse;
import flearn.entity.Course;
import flearn.enums.CourseStatus;
import flearn.common.exception.BusinessException;
import flearn.module.management.mapper.CourseMapper;
import flearn.repository.ClassroomRepository;
import flearn.repository.CourseRepository;
import flearn.module.management.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseMapper.toResponseList(courseRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    public List<CourseResponse> searchCourses(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllCourses();
        }
        return courseMapper.toResponseList(courseRepository.searchAll(keyword.trim()));
    }

    @Override
    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Khong tim thay khoa hoc."));
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    public void createCourse(CourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new BusinessException("Ma khoa hoc da ton tai.");
        }
        CourseStatus status = CourseStatus.ACTIVE;
        if (request.getStatus() != null) {
            try {
                status = CourseStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore and use default ACTIVE
            }
        }
        Course course = Course.builder()
                .courseCode(request.getCourseCode().trim())
                .courseName(request.getCourseName().trim())
                .description(request.getDescription())
                .status(status)
                .build();
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void updateCourse(Integer id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Khong tim thay khoa hoc."));
        if (!course.getCourseCode().equalsIgnoreCase(request.getCourseCode()) 
                && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new BusinessException("Ma khoa hoc da ton tai.");
        }
        course.setCourseCode(request.getCourseCode().trim());
        course.setCourseName(request.getCourseName().trim());
        course.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            try {
                course.setStatus(CourseStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Ignore and keep old status
            }
        }
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void toggleCourseStatus(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Khong tim thay khoa hoc."));
        if (course.getStatus() == CourseStatus.ACTIVE) {
            course.setStatus(CourseStatus.INACTIVE);
        } else {
            course.setStatus(CourseStatus.ACTIVE);
        }
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Khong tim thay khoa hoc."));
        if (classroomRepository.existsByCourse(course)) {
            throw new BusinessException("Khong the xoa khoa hoc nay vi da co lop hoc lien ket. Vui long thay doi trang thai khoa hoc.");
        }
        courseRepository.delete(course);
    }
}
