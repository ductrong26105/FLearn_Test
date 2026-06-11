package flearn.repository;

import flearn.entity.Classroom;
import flearn.enums.ClassStatus;
import flearn.entity.Course;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    boolean existsByCourse(Course course);

    List<Classroom> findByCourse(Course course);

    List<Classroom> findByTeacher(User teacher);

    List<Classroom> findByTeacherAndStatusNot(User teacher, ClassStatus status);

    long countByStatus(ClassStatus status);

    boolean existsByInviteCode(String inviteCode);

    Optional<Classroom> findByInviteCodeAndIsActiveTrue(String inviteCode);

    Optional<Classroom> findByInviteCode(String inviteCode);

    @Query("""
            SELECT c FROM Classroom c
            WHERE LOWER(c.className) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.inviteCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.teacher.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY c.createdAt DESC
            """)
    List<Classroom> searchAll(@Param("keyword") String keyword);
}
