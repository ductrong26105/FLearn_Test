package flearn.module.content.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningLessonRequest {
    @NotBlank(message = "Tên lesson không được để trống.")
    @Size(max = 200, message = "Tên lesson không được vượt quá 200 ký tự.")
    private String title;

    @Size(max = 5000, message = "Mô tả không được vượt quá 5000 ký tự.")
    private String description;

    @NotNull(message = "Thứ tự lesson không được để trống.")
    @Min(value = 0, message = "Thứ tự lesson phải >= 0.")
    private Integer orderIndex;

    @Size(max = 500, message = "URL video không được vượt quá 500 ký tự.")
    private String videoUrl;
}
