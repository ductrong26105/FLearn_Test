package flearn.module.management.service.impl;

import flearn.module.management.dto.request.RejectEnrollmentRequest;
import flearn.module.management.dto.response.EnrollmentResponse;
import flearn.entity.Classroom;
import flearn.entity.Enrollment;
import flearn.enums.EnrollmentStatus;
import flearn.entity.User;
import flearn.common.exception.BusinessException;
import flearn.module.management.mapper.EnrollmentMapper;
import flearn.repository.ClassroomRepository;
import flearn.repository.EnrollmentRepository;
import flearn.repository.UserRepository;
import flearn.common.service.EmailService;
import flearn.module.management.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final EmailService emailService;

    @Override
    public List<EnrollmentResponse> getClassEnrollments(Integer classId, User teacher) {
        Classroom classroom = findTeacherClass(classId, teacher);
        return enrollmentMapper.toResponseList(enrollmentRepository.findByClassRoomOrderByRequestedAtDesc(classroom));
    }

    @Override
    public List<EnrollmentResponse> searchActiveStudentsInClass(Integer classId, User teacher, String keyword) {
        Classroom classroom = findTeacherClass(classId, teacher);
        String normalized = keyword == null ? "" : keyword.trim();
        return enrollmentMapper.toResponseList(
                enrollmentRepository.searchStudentsInClass(classroom, EnrollmentStatus.ACTIVE, normalized)
        );
    }

    @Override
    @Transactional
    public void approveEnrollment(Integer classId, Integer enrollmentId, User teacher) {
        Enrollment enrollment = findEnrollmentInTeacherClass(classId, enrollmentId, teacher);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setApprovedAt(new Date());
        enrollment.setRejectReason(null);
        enrollmentRepository.save(enrollment);
        notifyStudent(enrollment, "FLearn - Yêu cầu vào lớp đã được chấp nhận",
                "Bạn đã được chấp nhận vào lớp: " + enrollment.getClassRoom().getClassName());
    }

    @Override
    @Transactional
    public void rejectEnrollment(Integer classId, Integer enrollmentId, User teacher, RejectEnrollmentRequest request) {
        Enrollment enrollment = findEnrollmentInTeacherClass(classId, enrollmentId, teacher);
        enrollment.setStatus(EnrollmentStatus.REJECTED);
        enrollment.setRejectReason(request.getRejectReason());
        enrollment.setRejectedAt(new Date());
        enrollmentRepository.save(enrollment);
        notifyStudent(enrollment, "FLearn - Yêu cầu vào lớp bị từ chối",
                "Yêu cầu vào lớp " + enrollment.getClassRoom().getClassName() + " bị từ chối. Lý do: " + request.getRejectReason());
    }

    @Override
    @Transactional
    public void removeStudent(Integer classId, Integer studentId, User teacher) {
        Classroom classroom = findTeacherClass(classId, teacher);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy học sinh."));
        Enrollment enrollment = enrollmentRepository.findByStudentAndClassRoom(student, classroom)
                .orElseThrow(() -> new BusinessException("Học sinh không thuộc lớp này."));
        enrollment.setStatus(EnrollmentStatus.OUT_OF_CLASS);
        enrollment.setRemovedAt(new Date());
        enrollmentRepository.save(enrollment);
    }

    private Enrollment findEnrollmentInTeacherClass(Integer classId, Integer enrollmentId, User teacher) {
        Classroom classroom = findTeacherClass(classId, teacher);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy yêu cầu tham gia."));
        if (!enrollment.getClassRoom().getClassId().equals(classroom.getClassId())) {
            throw new BusinessException("Yêu cầu tham gia không thuộc lớp này.");
        }
        return enrollment;
    }

    private Classroom findTeacherClass(Integer classId, User teacher) {
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy lớp học."));
        if (classroom.getTeacher() == null || !classroom.getTeacher().getUserId().equals(teacher.getUserId())) {
            throw new BusinessException("Bạn không có quyền thao tác lớp này.");
        }
        return classroom;
    }

    private void notifyStudent(Enrollment enrollment, String subject, String content) {
        try {
            emailService.sendEmail(enrollment.getStudent().getEmail(), subject, content);
        } catch (RuntimeException exception) {
            log.info("Mail fallback [{}] to {}: {}", subject, enrollment.getStudent().getEmail(), content);
        }
    }
}
