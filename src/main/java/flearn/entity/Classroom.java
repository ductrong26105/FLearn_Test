package flearn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "[Classes]") // Khớp với bảng Classes trong DB
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[ClassID]")
    private Integer classId;

    @Column(name = "[ClassName]", nullable = false, length = 100)
    private String className;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[TeacherID]", nullable = false)
    private User teacher;

    @Column(name = "[InviteCode]", unique = true, nullable = false, length = 10)
    private String inviteCode;

    @Column(name = "[IsActive]", nullable = false)
    private Boolean isActive = true;

    @Column(name = "[CreatedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}