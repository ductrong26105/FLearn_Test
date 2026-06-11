package flearn.repository;

import flearn.enums.ClassStatus;
import flearn.entity.Classroom;
import flearn.entity.Enrollment;
import flearn.enums.EnrollmentStatus;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    Optional<Enrollment> findByStudentAndClassRoom(User student, Classroom classRoom);

    boolean existsByStudentAndClassRoomAndStatusIn(User student, Classroom classRoom, List<EnrollmentStatus> statuses);

    boolean existsByStudentAndClassRoomAndStatus(User student, Classroom classRoom, EnrollmentStatus status);

    List<Enrollment> findByClassRoomOrderByRequestedAtDesc(Classroom classRoom);

    List<Enrollment> findByClassRoomAndStatus(Classroom classRoom, EnrollmentStatus status);

    @Query("""
            SELECT e FROM Enrollment e
            WHERE e.student = :student
              AND e.status = :enrollmentStatus
              AND e.classRoom.status = :classStatus
            ORDER BY e.requestedAt DESC
            """)
    List<Enrollment> findActiveClassesForStudent(@Param("student") User student,
                                                 @Param("enrollmentStatus") EnrollmentStatus enrollmentStatus,
                                                 @Param("classStatus") ClassStatus classStatus);

    @Query("""
            SELECT e FROM Enrollment e
            WHERE e.classRoom = :classRoom
              AND e.status = :status
              AND (LOWER(e.student.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(e.student.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(e.student.username) LIKE LOWER(CONCAT('%', :keyword, '%')))
            ORDER BY e.student.fullName
            """)
    List<Enrollment> searchStudentsInClass(@Param("classRoom") Classroom classRoom,
                                           @Param("status") EnrollmentStatus status,
                                           @Param("keyword") String keyword);
}
