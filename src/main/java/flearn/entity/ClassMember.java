package flearn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "[ClassMembers]") // Khớp với bảng ClassMembers trong SQL Server
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[MemberID]")
    private Integer memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[ClassID]", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[StudentID]", nullable = false)
    private User student;

    @Column(name = "[JoinedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt;

    @Column(name = "[Status]", nullable = false)
    private Integer status = 0; // 0: Chờ duyệt, 1: Đã duyệt, 2: Từ chối

    @PrePersist
    protected void onCreate() {
        joinedAt = new Date();
    }
}