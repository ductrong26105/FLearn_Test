package flearn.controller;

import flearn.entity.User;
import flearn.security.CustomUserDetails;
import flearn.service.ClassroomService;
import flearn.service.LessonService;
import flearn.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final ClassroomService classroomService;
    private final LessonService lessonService;
    private final QuizService quizService;

    @GetMapping("/dashboard")
    public String teacherDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User teacher = userDetails.getUser();
        model.addAttribute("teacherName", teacher.getFullName());
        model.addAttribute("classes", classroomService.getClassesByTeacher(teacher));
        return "teacher-dashboard";
    }

    @PostMapping("/create-class")
    public String createClass(@RequestParam String className,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        classroomService.createClass(className, userDetails.getUser());
        return "redirect:/teacher/dashboard";
    }

    // Nhớ import flearn.service.LessonService và khai báo nó ở đầu file (private final LessonService lessonService;)

    @GetMapping("/class/{id}")
    public String classDetail(@PathVariable("id") Integer classId, Model model) {
        // Tạm thời lấy danh sách bài giảng
        model.addAttribute("classId", classId);
        model.addAttribute("lessons", lessonService.getLessonsByClass(classId));
        return "teacher-class-detail";
    }

    @PostMapping("/class/{id}/add-lesson")
    public String addLesson(@PathVariable("id") Integer classId,
                            @RequestParam String title,
                            @RequestParam String content,
                            @RequestParam String videoUrl) {
        lessonService.createLesson(classId, title, content, videoUrl);
        return "redirect:/teacher/class/" + classId;
    }

    @PostMapping("/toggle-class")
    public String toggleClass(@RequestParam Integer classId) {
        classroomService.toggleClassStatus(classId);
        return "redirect:/teacher/dashboard";
    }

    // Thêm vào cuối file TeacherController.java
    @GetMapping("/class/{id}/approvals")
    public String viewApprovals(@PathVariable("id") Integer classId, Model model) {
        model.addAttribute("classId", classId);
        model.addAttribute("classroom", classroomService.getClassById(classId));
        model.addAttribute("pendingMembers", classroomService.getPendingMembers(classId));
        return "teacher-approvals";
    }

    @PostMapping("/class/{classId}/approve/{memberId}")
    public String approveStudent(@PathVariable("classId") Integer classId,
                                 @PathVariable("memberId") Integer memberId) {
        classroomService.approveMember(memberId);
        return "redirect:/teacher/class/" + classId + "/approvals";
    }

    @PostMapping("/class/{classId}/reject/{memberId}")
    public String rejectStudent(@PathVariable("classId") Integer classId,
                                @PathVariable("memberId") Integer memberId) {
        classroomService.rejectMember(memberId);
        return "redirect:/teacher/class/" + classId + "/approvals";
    }
    // Mở trang Quản lý Quiz của một bài học
    @GetMapping("/lesson/{lessonId}/quiz")
    public String manageQuiz(@PathVariable("lessonId") Integer lessonId, Model model) {
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("quiz", quizService.getQuizByLesson(lessonId));
        return "teacher-quiz-manager";
    }

    // Tạo tiêu đề Quiz
    @PostMapping("/lesson/{lessonId}/quiz/create")
    public String createQuiz(@PathVariable("lessonId") Integer lessonId, @RequestParam String title) {
        quizService.createQuiz(lessonId, title);
        return "redirect:/teacher/lesson/" + lessonId + "/quiz";
    }

    // Nhập 1 câu hỏi vào Quiz
    @PostMapping("/quiz/{quizId}/add-question")
    public String addQuestion(@PathVariable("quizId") Integer quizId,
                              @RequestParam Integer lessonId,
                              @RequestParam String questionText,
                              @RequestParam String optionA,
                              @RequestParam String optionB,
                              @RequestParam String optionC,
                              @RequestParam String optionD,
                              @RequestParam String correctAnswer) {
        quizService.addQuestion(quizId, questionText, optionA, optionB, optionC, optionD, correctAnswer);
        return "redirect:/teacher/lesson/" + lessonId + "/quiz";
    }
    // Mở trang xem bảng điểm của bài Quiz
    @GetMapping("/lesson/{lessonId}/quiz/results")
    public String viewQuizResults(@PathVariable("lessonId") Integer lessonId, Model model) {
        model.addAttribute("lessonId", lessonId);
        model.addAttribute("results", quizService.getResultsByLesson(lessonId));
        return "teacher-quiz-results";
    }
}