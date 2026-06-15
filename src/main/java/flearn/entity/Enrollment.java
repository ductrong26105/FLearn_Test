package flearn.entity;

import flearn.enums.EnrollmentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "[Enrollments]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[EnrollmentID]")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[StudentID]", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[ClassID]", nullable = false)
    private Classroom classRoom;

    @Enumerated(EnumType.STRING)
    @Column(name = "[Status]", nullable = false, length = 20)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Nationalized
    @Column(name = "[RequestMessage]", length = 500)
    private String requestMessage;

    @Nationalized
    @Column(name = "[RejectReason]", length = 500)
    private String rejectReason;

    @Column(name = "[RequestedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedAt;

    @Column(name = "[ApprovedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @Column(name = "[RejectedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedAt;

    @Column(name = "[RemovedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date removedAt;

    @PrePersist
    protected void onCreate() {
        requestedAt = new Date();
        if (status == null) {
            status = EnrollmentStatus.PENDING;
        }
    }
}
