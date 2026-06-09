// QuizRepository.java
package flearn.repository;
import flearn.entity.Lesson;
import flearn.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Optional<Quiz> findByLesson(Lesson lesson);
}