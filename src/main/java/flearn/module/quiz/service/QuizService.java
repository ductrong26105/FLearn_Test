package flearn.module.quiz.service;

import flearn.module.quiz.dto.request.QuestionRequest;
import flearn.module.quiz.dto.request.QuizRequest;
import flearn.module.quiz.dto.response.QuizResponse;
import flearn.module.quiz.dto.response.QuizResultResponse;
import flearn.entity.User;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface QuizService {
    List<QuizResponse> getTeacherQuizzesByLesson(Integer lessonId, User teacher);

    QuizResponse getTeacherQuiz(Integer quizId, User teacher);

    void createQuiz(Integer lessonId, User teacher, @Valid QuizRequest request);

    void updateQuiz(Integer quizId, User teacher, @Valid QuizRequest request);

    void deleteQuiz(Integer quizId, User teacher);

    void togglePublish(Integer quizId, User teacher);

    void createQuestion(Integer quizId, User teacher, @Valid QuestionRequest request);

    QuestionRequest getQuestionForEdit(Integer questionId, User teacher);

    Integer getQuestionQuizId(Integer questionId, User teacher);

    void updateQuestion(Integer questionId, User teacher, @Valid QuestionRequest request);

    void deleteQuestion(Integer questionId, User teacher);

    List<QuizResultResponse> getTeacherQuizResults(Integer quizId, User teacher);

    List<QuizResponse> getStudentQuizzesByLesson(Integer lessonId, User student);

    QuizResponse startQuiz(Integer quizId, User student);

    QuizResultResponse submitQuiz(Integer quizId, User student, Map<String, String> answers);

    List<QuizResultResponse> getStudentQuizHistory(User student);

    List<QuizResultResponse> getStudentQuizAttempts(Integer quizId, User student);
}
