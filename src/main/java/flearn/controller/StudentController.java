package flearn.controller;

import flearn.entity.User;
import flearn.entity.Quiz;
import flearn.security.CustomUserDetails;
import flearn.service.LessonService;
import flearn.service.StudentService;
import flearn.service.QuizService; // Import thêm
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final LessonService lessonService;
    private final QuizService quizService; // Tiêm QuizService vào đây

    @GetMapping("/dashboard")
    public String studentDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User student = userDetails.getUser();
        model.addAttribute("studentName", student.getFullName());
        model.addAttribute("joinedClasses", studentService.getJoinedClasses(student));
        return "student-dashboard";
    }

    // Cập nhật hàm xem bài giảng để truyền thêm dữ liệu Check trạng thái hoàn thành
    @GetMapping("/class/{id}")
    public String viewClassLessons(@PathVariable("id") Integer classId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {
        User student = userDetails.getUser();
        model.addAttribute("lessons", lessonService.getLessonsByClass(classId));
        model.addAttribute("quizService", quizService); // Đẩy service xuống để Thymeleaf gọi hàm check trực tiếp
        model.addAttribute("student", student);
        model.addAttribute("classId", classId);
        return "student-class-detail";
    }

    // Hàm xử lý bấm nút "Đánh dấu đã hoàn thành"
    @PostMapping("/lesson/{lessonId}/complete")
    public String completeLesson(@PathVariable Integer lessonId,
                                 @RequestParam Integer classId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        quizService.completeLesson(userDetails.getUser(), lessonId);
        return "redirect:/student/class/" + classId;
    }

    // Hàm mở trang làm bài Quiz
    @GetMapping("/lesson/{lessonId}/quiz")
    public String takeQuiz(@PathVariable Integer lessonId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {
        User student = userDetails.getUser();
        // Kiểm tra bảo mật bảo vệ: Chưa hoàn thành video bài giảng mà đòi vào thi thì chặn lại luôn
        if (!quizService.isLessonCompleted(student, lessonId)) {
            return "redirect:/student/dashboard";
        }

        Quiz quiz = quizService.getQuizByLesson(lessonId);
        if (quiz == null) {
            return "redirect:/student/dashboard";
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("lessonId", lessonId);
        return "student-quiz";
    }

    // Hàm nhận bài thi trắc nghiệm gửi lên và chấm điểm
    @PostMapping("/quiz/{quizId}/submit")
    public String submitQuiz(@PathVariable Integer quizId,
                             @RequestParam Integer lessonId,
                             @RequestParam Map<String, String> allParams,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {
        // allParams sẽ gom hết dữ liệu từ form, bao gồm cả token, đáp án...
        double score = quizService.submitQuiz(userDetails.getUser(), quizId, allParams);
        model.addAttribute("score", score);
        model.addAttribute("lessonId", lessonId);
        return "student-quiz-result";
    }
}