package flearn.controller;

import flearn.entity.User;
import flearn.security.CustomUserDetails;
import flearn.service.LessonService;
import flearn.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final LessonService lessonService;

    @GetMapping("/dashboard")
    public String studentDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User student = userDetails.getUser();
        model.addAttribute("studentName", student.getFullName());
        model.addAttribute("joinedClasses", studentService.getJoinedClasses(student));
        return "student-dashboard";
    }

    @GetMapping("/class/{id}")
    public String viewClassLessons(@PathVariable("id") Integer classId, Model model) {
        model.addAttribute("lessons", lessonService.getLessonsByClass(classId));
        return "student-class-detail";
    }

    // ĐÃ KÉO HÀM NÀY VÀO TRONG CLASS VÀ XÓA NGOẶC THỪA
    @PostMapping("/join-class")
    public String joinClass(@RequestParam String inviteCode,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            Model model) {
        try {
            studentService.joinClass(inviteCode, userDetails.getUser());
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            // Trả lại data nếu có lỗi để web không bị trắng
            User student = userDetails.getUser();
            model.addAttribute("studentName", student.getFullName());
            model.addAttribute("joinedClasses", studentService.getJoinedClasses(student));
            return "student-dashboard";
        }
        return "redirect:/student/dashboard";
    }
}