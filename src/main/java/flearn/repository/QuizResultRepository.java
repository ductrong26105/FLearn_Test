// QuizResultRepository.java
package flearn.repository;
import flearn.entity.Quiz;
import flearn.entity.QuizResult;
import flearn.entity.User;
import flearn.enums.QuizSubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Integer> {
    List<QuizResult> findByStudentAndQuizOrderByStartedAtDesc(User student, Quiz quiz);

    List<QuizResult> findByStudentOrderByStartedAtDesc(User student);

    List<QuizResult> findByQuizOrderByScoreDescSubmittedAtAsc(Quiz quiz);

    long countByStudentAndQuizAndStatusIn(User student, Quiz quiz, List<QuizSubmissionStatus> statuses);
}
