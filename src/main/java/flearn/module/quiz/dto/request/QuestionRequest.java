package flearn.module.quiz.dto.request;

import flearn.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    @NotNull(message = "Vui lòng chọn loại câu hỏi.")
    private QuestionType type;

    @NotBlank(message = "Nội dung câu hỏi không được để trống.")
    @Size(max = 2000, message = "Nội dung câu hỏi quá dài.")
    private String questionText;

    @Size(max = 500, message = "Đáp án A không được vượt quá 500 ký tự.")
    private String optionA;

    @Size(max = 500, message = "Đáp án B không được vượt quá 500 ký tự.")
    private String optionB;

    @Size(max = 500, message = "Đáp án C không được vượt quá 500 ký tự.")
    private String optionC;

    @Size(max = 500, message = "Đáp án D không được vượt quá 500 ký tự.")
    private String optionD;

    @NotBlank(message = "Đáp án đúng không được để trống.")
    @Pattern(regexp = "^(A|B|C|D|TRUE|FALSE)$", message = "Đáp án đúng không hợp lệ.")
    private String correctAnswer;

    private Integer orderIndex;
}
