package flearn.module.schedule.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClassScheduleRequest {

    private LocalDate scheduleDate;

    private List<Integer> daysOfWeek;

    @NotNull(message = "Vui lòng nhập giờ bắt đầu.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Size(max = 500, message = "Phòng/Link không được vượt quá 500 ký tự.")
    private String roomOrLink;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự.")
    private String note;

    private Boolean remindOneDayBefore   = true;
    private Boolean remindTwoHoursBefore = true;
    private Boolean isActive             = true;
}
