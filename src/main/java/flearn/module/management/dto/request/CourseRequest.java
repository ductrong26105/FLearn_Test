package flearn.module.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {
    @NotBlank(message = "Ma khoa hoc khong duoc de trong.")
    @Size(max = 50, message = "Ma khoa hoc khong duoc vuot qua 50 ky tu.")
    private String courseCode;

    @NotBlank(message = "Ten khoa hoc khong duoc de trong.")
    @Size(max = 100, message = "Ten khoa hoc khong duoc vuot qua 100 ky tu.")
    private String courseName;

    @Size(max = 500, message = "Mo ta khong duoc vuot qua 500 ky tu.")
    private String description;

    private String status;
}
