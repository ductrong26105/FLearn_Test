package flearn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "[Quizzes]")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[QuizID]")
    private Integer quizId;

    @Column(name = "[Title]", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[LessonID]", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;
}