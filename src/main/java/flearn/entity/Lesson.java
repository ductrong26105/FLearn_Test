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
    @JoinColumn(name = "[ClassID]")
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[RoadmapID]")
    private Roadmap roadmap;

    @Column(name = "[OrderIndex]")
    @Builder.Default
    private Integer orderIndex = 0;

    @Column(name = "[Visible]")
    @Builder.Default
    private Boolean visible = true;

    @Column(name = "[CreatedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "[UpdatedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
        syncLearningFields();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        syncLearningFields();
    }

    private void syncLearningFields() {
        if (visible == null) {
            visible = true;
        }
        if (orderIndex == null) {
            orderIndex = 0;
        }
        if (classroom == null && roadmap != null) {
            classroom = roadmap.getClassRoom();
        }
    }
}
