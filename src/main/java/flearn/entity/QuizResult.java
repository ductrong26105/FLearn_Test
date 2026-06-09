package flearn.entity;

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
    private Double score;

    @Column(name = "[CompletedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @PrePersist
    protected void onCreate() { completedAt = new Date(); }
}