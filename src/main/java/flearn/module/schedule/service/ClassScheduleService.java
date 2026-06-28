package flearn.module.schedule.service;

import flearn.module.schedule.dto.request.ClassScheduleRequest;
import flearn.module.schedule.dto.response.ClassScheduleResponse;
import flearn.entity.User;

import java.util.List;

public interface ClassScheduleService {
    List<ClassScheduleResponse> getSchedulesByClass(Integer classId);
    ClassScheduleResponse getScheduleById(Integer scheduleId);
    void createSchedule(Integer classId, ClassScheduleRequest request);
    void updateSchedule(Integer scheduleId, ClassScheduleRequest request);
    void toggleScheduleActive(Integer scheduleId);
    void deleteSchedule(Integer scheduleId);

    /** Teacher xem lịch tổng hợp tất cả lớp mình dạy. */
    List<ClassScheduleResponse> getSchedulesByTeacher(User teacher);

    /** Student xem lịch tổng hợp tất cả lớp đang theo học (enrollment ACTIVE). */
    List<ClassScheduleResponse> getSchedulesByStudent(User student);
}
