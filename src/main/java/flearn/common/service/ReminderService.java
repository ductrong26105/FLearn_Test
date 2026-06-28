package flearn.common.service;

import flearn.entity.*;
import flearn.enums.ClassStatus;
import flearn.enums.EnrollmentStatus;
import flearn.enums.QuizSubmissionStatus;
import flearn.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * ReminderService – Gửi email nhắc nhở lịch học.
 *
 * Cron chạy mỗi 5 phút, kiểm tra:
 *   1. Tìm buổi học tiếp theo của mỗi lịch học active
 *   2. Nếu buổi học còn đúng ~24 giờ → gửi nhắc "1 ngày trước"
 *   3. Nếu buổi học còn đúng ~2 tiếng → gửi nhắc "2 tiếng trước"
 *   4. Mỗi (lịch, sinh viên, loại nhắc, ngày học) chỉ gửi 1 lần (ReminderLog)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderService {

    private static final String TYPE_1DAY   = "1DAY";
    private static final String TYPE_2HOURS = "2HOURS";
    private static final String TYPE_2HOURS_ASSIGNMENT = "2HOURS";
    // Window cho phép: ±10 phút xung quanh mốc nhắc
    private static final long WINDOW_MINUTES = 10;

    private final ClassScheduleRepository classScheduleRepository;
    private final EnrollmentRepository    enrollmentRepository;
    private final ReminderLogRepository   reminderLogRepository;
    private final EmailService            emailService;
    private final QuizRepository          quizRepository;
    private final LessonRepository        lessonRepository;
    private final AssignmentReminderLogRepository assignmentReminderLogRepository;
    private final QuizResultRepository    quizResultRepository;
    private final MaterialRepository      materialRepository;
    private final MaterialTrackingRepository materialTrackingRepository;

    /**
     * Cron mỗi 5 phút kiểm tra và gửi email nhắc nhở.
     * fixedDelay tránh chạy chồng nếu lần trước chưa xong.
     */
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void checkAndSendReminders() {
        List<ClassSchedule> schedules = classScheduleRepository.findAllActive();
        LocalDateTime now = LocalDateTime.now();
        
        if (!schedules.isEmpty()) {
            log.debug("[Reminder] Cron triggered at {}, checking {} schedules", now, schedules.size());
            for (ClassSchedule schedule : schedules) {
                try {
                    processSchedule(schedule, now);
                } catch (Exception ex) {
                    log.error("[Reminder] Error processing schedule id={}: {}", schedule.getId(), ex.getMessage());
                }
            }
        }
        
        checkAndSendAssignmentReminders();
    }

    private void processSchedule(ClassSchedule schedule, LocalDateTime now) {
        // Lấy ngày giờ buổi học cụ thể
        LocalDateTime nextClass = LocalDateTime.of(schedule.getScheduleDate(), schedule.getStartTime());

        // Tính khoảng cách giờ tới buổi học
        long minutesUntilClass = java.time.Duration.between(now, nextClass).toMinutes();

        long twoHourMinutes  = 2 * 60;

        // Chỉ nhắc trước 2 tiếng
        boolean sendTwoHours = schedule.getRemindTwoHoursBefore()
                && minutesUntilClass >= (twoHourMinutes - WINDOW_MINUTES)
                && minutesUntilClass <= (twoHourMinutes + WINDOW_MINUTES);

        if (!sendTwoHours) return;

        // Lấy tất cả sinh viên active của lớp này
        List<User> students = enrollmentRepository
                .findByClassRoomAndStatus(schedule.getClassroom(), EnrollmentStatus.ACTIVE)
                .stream()
                .map(Enrollment::getStudent)
                .toList();

        LocalDate classDate = nextClass.toLocalDate();

        for (User student : students) {
            sendIfNotSent(schedule, student, TYPE_2HOURS, classDate, nextClass);
        }
    }

    private void sendIfNotSent(ClassSchedule schedule, User student,
                                String type, LocalDate classDate, LocalDateTime nextClass) {
        if (student.getEmail() == null || student.getEmail().isBlank()) return;

        boolean alreadySent = reminderLogRepository.existsByScheduleAndUserAndReminderTypeAndScheduledDate(
                schedule, student, type, classDate);
        if (alreadySent) return;

        // Gửi email
        String subject = buildSubject(type, schedule);
        String body    = buildBody(type, schedule, student, nextClass);

        try {
            emailService.sendEmail(student.getEmail(), subject, body);

            // Ghi log để không gửi lại
            reminderLogRepository.save(ReminderLog.builder()
                    .schedule(schedule)
                    .user(student)
                    .reminderType(type)
                    .scheduledDate(classDate)
                    .sentToEmail(student.getEmail())
                    .build());

            log.info("[Reminder] Sent {} reminder → {} for schedule {}", type, student.getEmail(), schedule.getId());
        } catch (Exception ex) {
            log.error("[Reminder] Failed to send email to {}: {}", student.getEmail(), ex.getMessage());
        }
    }

    // Xóa nextOccurrence() vì giờ đã dùng scheduleDate cụ thể

    private String buildSubject(String type, ClassSchedule schedule) {
        return String.format("[FLearn] Nhắc nhở: Lớp \"%s\" sẽ bắt đầu sau 2 tiếng",
                schedule.getClassroom().getClassName());
    }

    private String buildBody(String type, ClassSchedule schedule, User student, LocalDateTime nextClass) {
        String roomOrLink = schedule.getRoomOrLink() != null ? schedule.getRoomOrLink() : "Chưa cập nhật";
        String note = schedule.getNote() != null ? "\nNội dung: " + schedule.getNote() : "";

        return String.format(
            "Xin chào %s,\n\n" +
            "Lớp học \"%s\" của bạn sẽ bắt đầu sau 2 tiếng nữa.\n\n" +
            "📅 Ngày: %s\n" +
            "⏰ Giờ: %s – %s\n" +
            "📍 Phòng/Link: %s%s\n\n" +
            "Hãy chuẩn bị sẵn sàng để tham gia buổi học!\n\n" +
            "Trân trọng,\nFLearn System",
            student.getFullName(),
            schedule.getClassroom().getClassName(),
            nextClass.toLocalDate(),
            schedule.getStartTime(),
            schedule.getEndTime() != null ? schedule.getEndTime().toString() : "?",
            roomOrLink,
            note
        );
    }

    private void checkAndSendAssignmentReminders() {
        LocalDateTime now = LocalDateTime.now();
        // 2 hour window +/- WINDOW_MINUTES
        LocalDateTime startWindow = now.plusHours(2).minusMinutes(WINDOW_MINUTES);
        LocalDateTime endWindow = now.plusHours(2).plusMinutes(WINDOW_MINUTES);
        
        Date start = Date.from(startWindow.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endWindow.atZone(ZoneId.systemDefault()).toInstant());

        // Quizzes
        List<Quiz> upcomingQuizzes = quizRepository.findUpcomingDeadlines(start, end);
        for (Quiz quiz : upcomingQuizzes) {
            if (quiz.getLesson() != null && quiz.getLesson().getClassroom() != null) {
                List<User> students = enrollmentRepository
                        .findByClassRoomAndStatus(quiz.getLesson().getClassroom(), EnrollmentStatus.ACTIVE)
                        .stream().map(Enrollment::getStudent).toList();
                
                for (User student : students) {
                    processQuizReminder(quiz, student);
                }
            }
        }

        // Lessons
        List<Lesson> upcomingLessons = lessonRepository.findUpcomingDeadlines(start, end);
        for (Lesson lesson : upcomingLessons) {
            if (lesson.getClassroom() != null) {
                List<User> students = enrollmentRepository
                        .findByClassRoomAndStatus(lesson.getClassroom(), EnrollmentStatus.ACTIVE)
                        .stream().map(Enrollment::getStudent).toList();
                
                for (User student : students) {
                    processLessonReminder(lesson, student);
                }
            }
        }
    }
    
    private void processQuizReminder(Quiz quiz, User student) {
        if (student.getEmail() == null || student.getEmail().isBlank()) return;

        // Check if already sent
        boolean alreadySent = assignmentReminderLogRepository.existsByQuizAndUserAndReminderType(quiz, student, TYPE_2HOURS_ASSIGNMENT);
        if (alreadySent) return;

        // Check if already completed
        long submittedCount = quizResultRepository.countByStudentAndQuizAndStatusIn(
                student, quiz, Arrays.asList(QuizSubmissionStatus.SUBMITTED, QuizSubmissionStatus.LATE)
        );
        if (submittedCount > 0) return; // Student already submitted

        String subject = String.format("[FLearn] Nhắc nhở hạn chót: Bài tập \"%s\" sắp hết hạn", quiz.getTitle());
        String body = String.format(
                "Xin chào %s,\n\n" +
                "Bạn có bài tập \"%s\" thuộc lớp \"%s\" sẽ hết hạn vào lúc %s.\n" +
                "Hãy hoàn thành bài tập trước hạn chót nhé!\n\n" +
                "Trân trọng,\nFLearn System",
                student.getFullName(),
                quiz.getTitle(),
                quiz.getLesson().getClassroom().getClassName(),
                quiz.getDeadline().toString()
        );

        sendAndLogAssignmentReminder(student, subject, body, quiz, null);
    }
    
    private void processLessonReminder(Lesson lesson, User student) {
        if (student.getEmail() == null || student.getEmail().isBlank()) return;

        // Check if already sent
        boolean alreadySent = assignmentReminderLogRepository.existsByLessonAndUserAndReminderType(lesson, student, TYPE_2HOURS_ASSIGNMENT);
        if (alreadySent) return;

        // Check if already completed (viewed all published materials)
        List<Material> materials = materialRepository.findByLessonAndPublishedTrueOrderByCreatedAtAsc(lesson);
        if (!materials.isEmpty()) {
            boolean allViewed = true;
            for (Material material : materials) {
                boolean viewed = materialTrackingRepository.findByStudentAndMaterial(student, material)
                        .map(MaterialTracking::getViewed)
                        .orElse(false);
                if (!viewed) {
                    allViewed = false;
                    break;
                }
            }
            if (allViewed) return; // Already completed all materials
        }

        String subject = String.format("[FLearn] Nhắc nhở hạn chót: Bài học \"%s\" sắp hết hạn", lesson.getTitle());
        String body = String.format(
                "Xin chào %s,\n\n" +
                "Bạn có bài học \"%s\" thuộc lớp \"%s\" sẽ hết hạn vào lúc %s.\n" +
                "Hãy hoàn thành các tài liệu của bài học trước hạn chót nhé!\n\n" +
                "Trân trọng,\nFLearn System",
                student.getFullName(),
                lesson.getTitle(),
                lesson.getClassroom().getClassName(),
                lesson.getDeadline().toString()
        );

        sendAndLogAssignmentReminder(student, subject, body, null, lesson);
    }
    
    private void sendAndLogAssignmentReminder(User student, String subject, String body, Quiz quiz, Lesson lesson) {
        try {
            emailService.sendEmail(student.getEmail(), subject, body);

            assignmentReminderLogRepository.save(AssignmentReminderLog.builder()
                    .quiz(quiz)
                    .lesson(lesson)
                    .user(student)
                    .reminderType(TYPE_2HOURS_ASSIGNMENT)
                    .sentToEmail(student.getEmail())
                    .build());

            log.info("[Reminder] Sent 1HOUR assignment reminder → {} for quiz={}, lesson={}", 
                    student.getEmail(), 
                    quiz != null ? quiz.getQuizId() : "N/A", 
                    lesson != null ? lesson.getLessonId() : "N/A");
        } catch (Exception ex) {
            log.error("[Reminder] Failed to send assignment email to {}: {}", student.getEmail(), ex.getMessage());
        }
    }
}
