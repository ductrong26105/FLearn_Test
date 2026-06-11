package flearn.repository;

import flearn.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByCourseCode(String courseCode);

    Optional<Course> findByCourseCode(String courseCode);

    List<Course> findAllByOrderByCreatedAtDesc();

    @Query("""
            SELECT c FROM Course c
            WHERE LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY c.createdAt DESC
            """)
    List<Course> searchAll(@Param("keyword") String keyword);
}
