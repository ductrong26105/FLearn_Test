// LessonCompletionRepository.java
package flearn.repository;
import flearn.entity.Lesson;
import flearn.entity.LessonCompletion;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, Integer> {
    boolean existsByStudentAndLesson(User student, Lesson lesson);
}