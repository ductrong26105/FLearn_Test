// QuizRepository.java
package flearn.repository;
import flearn.entity.Lesson;
import flearn.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByLessonOrderByCreatedAtDesc(Lesson lesson);

    List<Quiz> findByLessonAndPublishedTrueOrderByCreatedAtDesc(Lesson lesson);

    boolean existsByLesson(Lesson lesson);

    Optional<Quiz> findByLesson(Lesson lesson);
}
