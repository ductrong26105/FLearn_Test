package flearn.entity;

import flearn.enums.CourseStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "[Courses]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[CourseID]")
    private Integer id;

    @Column(name = "[CourseCode]", unique = true, nullable = false, length = 50)
    private String courseCode;

    @Nationalized
    @Column(name = "[CourseName]", nullable = false, length = 100)
    private String courseName;

    @Nationalized
    @Column(name = "[Description]", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "[Status]", length = 20)
    @Builder.Default
    private CourseStatus status = CourseStatus.ACTIVE;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
