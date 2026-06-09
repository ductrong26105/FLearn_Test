package flearn.service;

import flearn.entity.*;
import flearn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;
    private final LessonCompletionRepository lessonCompletionRepository;
    private final QuizResultRepository quizResultRepository;

    // 1. Kiểm tra xem sinh viên đã bấm hoàn thành bài học chưa
    public boolean isLessonCompleted(User student, Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson == null) return false;
        return lessonCompletionRepository.existsByStudentAndLesson(student, lesson);
    }

    // 2. Xử lý khi sinh viên bấm nút "Đánh dấu đã hoàn thành"
    public void completeLesson(User student, Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học"));

        if (!lessonCompletionRepository.existsByStudentAndLesson(student, lesson)) {
            LessonCompletion completion = LessonCompletion.builder()
                    .student(student)
                    .lesson(lesson)
                    .build();
            lessonCompletionRepository.save(completion);
        }
    }

    public Quiz getQuizByLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        return quizRepository.findByLesson(lesson).orElse(null);
    }

    // 3. Xử lý chấm điểm bài Quiz tự động
    public double submitQuiz(User student, Integer quizId, Map<String, String> answers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài kiểm tra"));

        int totalQuestions = quiz.getQuestions().size();
        if (totalQuestions == 0) return 0.0;

        int correctCount = 0;
        for (Question q : quiz.getQuestions()) {
            // Nhận đáp án sinh viên chọn từ Form (Name của input sẽ là "question_ID")
            String studentAnswer = answers.get("question_" + q.getQuestionId());
            if (studentAnswer != null && studentAnswer.trim().equalsIgnoreCase(q.getCorrectAnswer().trim())) {
                correctCount++;
            }
        }

        double finalScore = ((double) correctCount / totalQuestions) * 10;
        // Làm tròn 1 chữ số thập phân
        finalScore = Math.round(finalScore * 10.0) / 10.0;

        // Lưu hoặc cập nhật điểm số
        QuizResult result = quizResultRepository.findByStudentAndQuiz(student, quiz)
                .orElse(QuizResult.builder().student(student).quiz(quiz).build());
        result.setScore(finalScore);
        quizResultRepository.save(result);

        return finalScore;
    }
    public void createQuiz(Integer lessonId, String title) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học"));
        Quiz newQuiz = Quiz.builder()
                .lesson(lesson)
                .title(title)
                .build();
        quizRepository.save(newQuiz);
    }

    // Thêm 1 câu hỏi vào bài kiểm tra
    public void addQuestion(Integer quizId, String text, String optA, String optB, String optC, String optD, String correct) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài kiểm tra"));
        Question question = Question.builder()
                .quiz(quiz)
                .questionText(text)
                .optionA(optA)
                .optionB(optB)
                .optionC(optC)
                .optionD(optD)
                .correctAnswer(correct)
                .build();
        questionRepository.save(question);
    }
    // Lấy danh sách điểm số sinh viên đã làm của bài học
    public List<QuizResult> getResultsByLesson(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài học"));
        Quiz quiz = quizRepository.findByLesson(lesson).orElse(null);
        if (quiz == null) return java.util.Collections.emptyList();

        return quizResultRepository.findByQuizOrderByScoreDesc(quiz);
    }
}