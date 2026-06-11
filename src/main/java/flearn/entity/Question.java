package flearn.entity;

import flearn.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "[Questions]")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[QuestionID]")
    private Integer questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[QuizID]", nullable = false)
    private Quiz quiz;

    @Enumerated(EnumType.STRING)
    @Column(name = "[QuestionType]", nullable = false, length = 30)
    @Builder.Default
    private QuestionType type = QuestionType.MULTIPLE_CHOICE;

    @Column(name = "[QuestionText]", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String questionText;

    @Column(name = "[OptionA]", columnDefinition = "NVARCHAR(500)")
    private String optionA;

    @Column(name = "[OptionB]", columnDefinition = "NVARCHAR(500)")
    private String optionB;

    @Column(name = "[OptionC]", columnDefinition = "NVARCHAR(500)")
    private String optionC;

    @Column(name = "[OptionD]", columnDefinition = "NVARCHAR(500)")
    private String optionD;

    @Column(name = "[CorrectAnswer]", nullable = false, length = 10)
    private String correctAnswer;

    @Column(name = "[OrderIndex]")
    @Builder.Default
    private Integer orderIndex = 0;

    @PrePersist
    @PreUpdate
    protected void syncDefaults() {
        if (type == null) {
            type = QuestionType.MULTIPLE_CHOICE;
        }
        if (orderIndex == null) {
            orderIndex = 0;
        }
    }
}
