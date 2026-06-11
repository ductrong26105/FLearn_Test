package flearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "[MaterialTrackings]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[TrackingID]")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[StudentID]", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "[MaterialID]", nullable = false)
    private Material material;

    @Column(name = "[Viewed]", nullable = false)
    @Builder.Default
    private Boolean viewed = true;

    @Column(name = "[ViewedAt]", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date viewedAt;

    @PrePersist
    protected void onCreate() {
        if (viewed == null) {
            viewed = true;
        }
        if (viewedAt == null) {
            viewedAt = new Date();
        }
    }
}
