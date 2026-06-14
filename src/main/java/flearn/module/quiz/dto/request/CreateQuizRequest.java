package flearn.module.quiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizRequest {
    @Positive(message = "Ma bai hoc khong hop le.")
    private Integer lessonId;

    @NotBlank(message = "Ten bai kiem tra khong duoc de trong.")
    @Size(max = 200, message = "Ten bai kiem tra khong duoc vuot qua 200 ky tu.")
    private String title;

    private String description;
    private Integer timeLimitMinutes;
    private java.util.Date deadline;
    private Boolean shuffleQuestions;
    private Boolean shuffleAnswers;
    private Integer maxAttempts;
}
