package flearn.repository;

import flearn.entity.User;
import flearn.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :roleId")
    long countUsersByRole(@Param("roleId") Integer roleId);

    List<User> findByRole(Integer role);

    long countByRole(Integer role);

    long countByRoleAndStatus(Integer role, UserStatus status);

    @Query("""
            SELECT u FROM User u
            WHERE u.role = :role
              AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))
            ORDER BY u.createdAt DESC
            """)
    List<User> searchByRole(@Param("role") Integer role, @Param("keyword") String keyword);

    long countByCreatedAtAfter(java.util.Date date);
}
