package flearn.module.quiz.dto.response;

import flearn.enums.QuizSubmissionStatus;
import flearn.module.auth.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultResponse {
    private Integer resultId;
    private UserResponse student;
    private QuizResponse quiz;
    private Double score;
    private Integer correctCount;
    private Integer totalQuestions;
    private Integer attemptNo;
    private QuizSubmissionStatus status;
    private Date startedAt;
    private Date submittedAt;
    private Date completedAt;
}
