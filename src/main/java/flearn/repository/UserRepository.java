package flearn.repository;

import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :roleId")
    long countUsersByRole(@Param("roleId") Integer roleId);
}