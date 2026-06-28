package flearn.module.schedule.service.impl;

import flearn.common.exception.BusinessException;
import flearn.entity.ClassSchedule;
import flearn.entity.Classroom;
import flearn.entity.Enrollment;
import flearn.entity.User;
import flearn.enums.EnrollmentStatus;
import flearn.module.schedule.dto.request.ClassScheduleRequest;
import flearn.module.schedule.dto.response.ClassScheduleResponse;
import flearn.module.schedule.service.ClassScheduleService;
import flearn.repository.ClassScheduleRepository;
import flearn.repository.ClassroomRepository;
import flearn.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import flearn.repository.AttendanceRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class ClassScheduleServiceImpl implements ClassScheduleService {

    private final ClassScheduleRepository scheduleRepository;
    private final ClassroomRepository     classroomRepository;
    private final EnrollmentRepository    enrollmentRepository;
    private final AttendanceRepository    attendanceRepository;

    @Override
    public List<ClassScheduleResponse> getSchedulesByClass(Integer classId) {
        Classroom classroom = findClassById(classId);
        return scheduleRepository
                .findByClassroomAndIsActiveTrueOrderByScheduleDateAscStartTimeAsc(classroom)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ClassScheduleResponse getScheduleById(Integer scheduleId) {
        return toResponse(findScheduleById(scheduleId));
    }

    @Override
    @Transactional
    public void createSchedule(Integer classId, ClassScheduleRequest request) {
        Classroom classroom = findClassById(classId);
        if (classroom.getStartDate() == null || classroom.getEndDate() == null) {
            throw new BusinessException("Lớp học phải có Ngày bắt đầu và Ngày kết thúc mới có thể tự động tạo lịch học.");
        }
        if (request.getDaysOfWeek() == null || request.getDaysOfWeek().isEmpty()) {
            throw new BusinessException("Vui lòng chọn ít nhất một thứ trong tuần.");
        }

        java.time.LocalDate current = classroom.getStartDate();
        java.time.LocalDate end = classroom.getEndDate();

        while (!current.isAfter(end)) {
            // DayOfWeek.getValue() trả về 1=Monday, 2=Tuesday... 7=Sunday
            // Nhưng trong form của chúng ta: 2=T2, 3=T3, ..., 7=T7, 1=CN
            int currentDowValue = current.getDayOfWeek().getValue();
            int myDowValue = (currentDowValue == 7) ? 1 : currentDowValue + 1; // Convert từ chuẩn Java sang chuẩn của form (1=CN, 2=T2, ...)

            if (request.getDaysOfWeek().contains(myDowValue)) {
                ClassSchedule schedule = ClassSchedule.builder()
                        .classroom(classroom)
                        .scheduleDate(current)
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .roomOrLink(request.getRoomOrLink())
                        .note(request.getNote())
                        .remindOneDayBefore(Boolean.TRUE.equals(request.getRemindOneDayBefore()))
                        .remindTwoHoursBefore(Boolean.TRUE.equals(request.getRemindTwoHoursBefore()))
                        .isActive(Boolean.TRUE.equals(request.getIsActive()))
                        .build();
                scheduleRepository.save(schedule);
            }
            current = current.plusDays(1);
        }
    }

    @Override
    @Transactional
    public void updateSchedule(Integer scheduleId, ClassScheduleRequest request) {
        ClassSchedule schedule = findScheduleById(scheduleId);
        // Không cho sửa ngày (scheduleDate) từ form edit hiện tại
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setRoomOrLink(request.getRoomOrLink());
        schedule.setNote(request.getNote());
        schedule.setRemindOneDayBefore(Boolean.TRUE.equals(request.getRemindOneDayBefore()));
        schedule.setRemindTwoHoursBefore(Boolean.TRUE.equals(request.getRemindTwoHoursBefore()));
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void toggleScheduleActive(Integer scheduleId) {
        ClassSchedule schedule = findScheduleById(scheduleId);
        schedule.setIsActive(!Boolean.TRUE.equals(schedule.getIsActive()));
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Integer scheduleId) {
        scheduleRepository.delete(findScheduleById(scheduleId));
    }

    @Override
    public List<ClassScheduleResponse> getSchedulesByTeacher(User teacher) {
        return scheduleRepository
                .findByClassroom_TeacherAndIsActiveTrueOrderByScheduleDateAscStartTimeAsc(teacher)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ClassScheduleResponse> getSchedulesByStudent(User student) {
        // Lấy danh sách classroom từ enrollment ACTIVE
        List<Classroom> classrooms = enrollmentRepository
                .findByStudentAndStatus(student, EnrollmentStatus.ACTIVE)
                .stream()
                .map(Enrollment::getClassRoom)
                .toList();

        if (classrooms.isEmpty()) return List.of();

        List<ClassSchedule> schedules = scheduleRepository
                .findByClassroomInAndIsActiveTrueOrderByScheduleDateAscStartTimeAsc(classrooms);
                
        if (schedules.isEmpty()) return List.of();

        // Lấy danh sách điểm danh của student trong các schedule này
        List<flearn.entity.Attendance> attendances = attendanceRepository.findByStudentAndScheduleIn(student, schedules);
        Map<Integer, flearn.enums.AttendanceStatus> attendanceMap = attendances.stream()
                .collect(Collectors.toMap(
                        a -> a.getSchedule().getId(),
                        a -> a.getStatus()
                ));

        return schedules.stream()
                .map(s -> {
                    ClassScheduleResponse res = toResponse(s);
                    res.setAttendanceStatus(attendanceMap.getOrDefault(s.getId(), flearn.enums.AttendanceStatus.NOT_YET));
                    return res;
                })
                .toList();
    }

    // ─────────────── Helpers ───────────────

    private ClassSchedule findScheduleById(Integer id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy lịch học."));
    }

    private Classroom findClassById(Integer classId) {
        return classroomRepository.findById(classId)
                .orElseThrow(() -> new BusinessException("Không tìm thấy lớp học."));
    }

    private ClassScheduleResponse toResponse(ClassSchedule s) {
        String teacherName = null;
        if (s.getClassroom().getTeacher() != null) {
            teacherName = s.getClassroom().getTeacher().getFullName();
        }
        
        int dowValue = s.getScheduleDate().getDayOfWeek().getValue();
        int myDowValue = (dowValue == 7) ? 1 : dowValue + 1; // Convert từ chuẩn Java (7=CN) sang chuẩn form (1=CN)

        return ClassScheduleResponse.builder()
                .id(s.getId())
                .classId(s.getClassroom().getClassId())
                .className(s.getClassroom().getClassName())
                .scheduleDate(s.getScheduleDate())
                .dayOfWeekLabel(ClassScheduleResponse.dayLabel(myDowValue))
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .roomOrLink(s.getRoomOrLink())
                .note(s.getNote())
                .remindOneDayBefore(s.getRemindOneDayBefore())
                .remindTwoHoursBefore(s.getRemindTwoHoursBefore())
                .isActive(s.getIsActive())
                .teacherName(teacherName)
                .createdAt(s.getCreatedAt())
                .build();
    }
}
