package flearn.entity;

import flearn.enums.Role;
import flearn.enums.UserStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "[Users]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[UserID]")
    private Integer userId;

    @Column(name = "[Username]", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "[PasswordHash]", nullable = false, length = 255)
    private String passwordHash;

    @Nationalized
    @Column(name = "[FullName]", nullable = false, length = 100)
    private String fullName;

    @Column(name = "[Email]", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "[Phone]", length = 20)
    private String phone;

    @Column(name = "[Role]", nullable = false)
    private Integer role;

    @Enumerated(EnumType.STRING)
    @Column(name = "[Status]", length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Nationalized
    @Column(name = "[Department]", length = 100)
    private String department;

    @Column(name = "[IsActive]", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "[CreatedAt]", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "[UpdatedAt]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "[ResetToken]", length = 100)
    private String resetToken;

    @Column(name = "[ResetTokenExpiry]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetTokenExpiry;

    @Column(name = "[OtpCode]", length = 10)
    private String otpCode;

    @Column(name = "[OtpExpiry]")
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpExpiry;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;
        syncStatusFields();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
        syncStatusFields();
    }

    public Role getRoleType() {
        return Role.fromCode(role);
    }

    public void setRoleType(Role role) {
        this.role = role.getCode();
    }

    public boolean isBlocked() {
        return status == UserStatus.BLOCKED || Boolean.FALSE.equals(isActive);
    }

    private void syncStatusFields() {
        if (status == null) {
            status = Boolean.FALSE.equals(isActive) ? UserStatus.BLOCKED : UserStatus.ACTIVE;
        }
        if (isActive == null) {
            isActive = status != UserStatus.BLOCKED;
        } else if (status == UserStatus.BLOCKED) {
            isActive = false;
        } else if (status == UserStatus.ACTIVE) {
            isActive = true;
        }
    }
}
