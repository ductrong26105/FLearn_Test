package flearn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * ClassSchedule – Lịch học cụ thể của một lớp theo từng ngày.
 * Admin cấu hình sinh tự động, sau đó có thể sửa từng buổi.
 * Hệ thống tự gửi email nhắc nhở trước 2 tiếng.
 */
@Entity
@Table(name = "[ClassSchedules]")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[ScheduleID]")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[ClassID]", nullable = false)
    private Classroom classroom;

    /**
     * Ngày học cụ thể (VD: 2026-06-22).
     */
    @Column(name = "[ScheduleDate]", nullable = false)
    private LocalDate scheduleDate;

    /** Giờ bắt đầu buổi học (VD: 08:00). */
    @Column(name = "[StartTime]", nullable = false)
    private LocalTime startTime;

    /** Giờ kết thúc buổi học (VD: 10:00). */
    @Column(name = "[EndTime]")
    private LocalTime endTime;

    /** Phòng học hoặc link Google Meet/Zoom. */
    @Column(name = "[RoomOrLink]", length = 500)
    private String roomOrLink;

    /** Mô tả ngắn về nội dung buổi học (tùy chọn). */
    @Column(name = "[Note]", length = 500)
    private String note;

    /** Gửi email nhắc trước 1 ngày. */
    @Column(name = "[RemindOneDayBefore]", nullable = false)
    @Builder.Default
    private Boolean remindOneDayBefore = true;

    /** Gửi email nhắc trước 2 tiếng. */
    @Column(name = "[RemindTwoHoursBefore]", nullable = false)
    @Builder.Default
    private Boolean remindTwoHoursBefore = true;

    /** Lịch còn hoạt động hay không (ẩn lịch mà không xóa). */
    @Column(name = "[IsActive]", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "[CreatedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "[UpdatedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
        if (remindOneDayBefore == null) remindOneDayBefore = true;
        if (remindTwoHoursBefore == null) remindTwoHoursBefore = true;
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
