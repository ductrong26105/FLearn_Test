package flearn.entity;

import flearn.enums.QuizSubmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "[QuizResults]")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[ResultID]")
    private Integer resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[StudentID]", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[QuizID]", nullable = false)
    private Quiz quiz;

    @Column(name = "[Score]", nullable = false)
    @Builder.Default
    private Double score = 0.0;

    @Column(name = "[CorrectCount]")
    @Builder.Default
    private Integer correctCount = 0;

    @Column(name = "[TotalQuestions]")
    @Builder.Default
    private Integer totalQuestions = 0;

    @Column(name = "[AttemptNo]")
    @Builder.Default
    private Integer attemptNo = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "[Status]", length = 20, nullable = false)
    @Builder.Default
    private QuizSubmissionStatus status = QuizSubmissionStatus.IN_PROGRESS;

    @Column(name = "[StartedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;

    @Column(name = "[SubmittedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Column(name = "[CompletedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = new Date();
        }
        if (score == null) {
            score = 0.0;
        }
        if (correctCount == null) {
            correctCount = 0;
        }
        if (totalQuestions == null) {
            totalQuestions = 0;
        }
        if (attemptNo == null) {
            attemptNo = 1;
        }
        if (status == null) {
            status = QuizSubmissionStatus.IN_PROGRESS;
        }
    }
}
