package flearn.entity;

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

    @Column(name = "[QuestionText]", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String questionText;

    @Column(name = "[OptionA]", nullable = false, columnDefinition = "NVARCHAR(500)")
    private String optionA;

    @Column(name = "[OptionB]", nullable = false, columnDefinition = "NVARCHAR(500)")
    private String optionB;

    @Column(name = "[OptionC]", nullable = false, columnDefinition = "NVARCHAR(500)")
    private String optionC;

    @Column(name = "[OptionD]", nullable = false, columnDefinition = "NVARCHAR(500)")
    private String optionD;

    @Column(name = "[CorrectAnswer]", nullable = false, length = 1)
    private String correctAnswer; // "A", "B", "C", "D"
}