package flearn.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "[Users]") // Ép dùng đúng bảng Users
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[UserID]")
    private Integer userId;

    @Column(name = "[Username]", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "[PasswordHash]", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "[FullName]", nullable = false, length = 100)
    private String fullName;

    @Column(name = "[Email]", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "[Role]", nullable = false)
    private Integer role;

    @Column(name = "[IsActive]", nullable = false)
    private Boolean isActive = true;

    @Column(name = "[CreatedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // ResetToken và ResetTokenExpiry vì nó cho phép NULL trong DB của bạn
    @Column(name = "[ResetToken]", length = 100)
    private String resetToken;

    @Column(name = "[ResetTokenExpiry]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetTokenExpiry;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}