package flearn.repository;

import flearn.entity.Question;
import flearn.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByQuizOrderByOrderIndexAscQuestionIdAsc(Quiz quiz);
}
