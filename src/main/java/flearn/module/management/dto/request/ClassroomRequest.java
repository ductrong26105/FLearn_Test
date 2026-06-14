package flearn.module.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequest {
    @NotBlank(message = "Ten lop khong duoc de trong.")
    @Size(max = 100, message = "Ten lop khong duoc vuot qua 100 ky tu.")
    private String className;

    @Size(max = 500, message = "Mo ta khong duoc vuot qua 500 ky tu.")
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @jakarta.validation.constraints.NotNull(message = "Vui long chon khoa hoc.")
    private Integer courseId;

    private Integer teacherId;
}
