// QuizResultRepository.java
package flearn.repository;
import flearn.entity.Quiz;
import flearn.entity.QuizResult;
import flearn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizResultRepository extends JpaRepository<QuizResult, Integer> {
    Optional<QuizResult> findByStudentAndQuiz(User student, Quiz quiz);
    List<QuizResult> findByQuizOrderByScoreDesc(Quiz quiz);
}