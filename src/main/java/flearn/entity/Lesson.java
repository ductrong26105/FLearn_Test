package flearn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "[Lessons]")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[LessonID]")
    private Integer lessonId;

    @Column(name = "[Title]", nullable = false, length = 200)
    private String title;

    @Column(name = "[Content]", columnDefinition = "NVARCHAR(MAX)")
    private String content; // Ghi chú hoặc tóm tắt bài học

    @Column(name = "[VideoUrl]", length = 500)
    private String videoUrl; // Lưu link nhúng YouTube hoặc Google Drive

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[ClassID]", nullable = false)
    private Classroom classroom;

    @Column(name = "[CreatedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}