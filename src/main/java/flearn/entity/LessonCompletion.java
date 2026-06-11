package flearn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "[LessonCompletions]")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[CompletionID]")
    private Integer completionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[StudentID]", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[LessonID]", nullable = false)
    private Lesson lesson;

    @Column(name = "[CompletedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @PrePersist
    protected void onCreate() { completedAt = new Date(); }
}