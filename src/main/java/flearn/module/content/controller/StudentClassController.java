package flearn.module.content.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.content.service.LearningContentService;
import flearn.module.quiz.service.QuizService;
import flearn.module.management.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentClassController {
    private final StudentService studentService;
    private final LearningContentService learningContentService;
    private final QuizService quizService;

    @GetMapping({"", "/dashboard"})
    public String studentDashboard() {
        return "redirect:/student/classes";
    }

    @GetMapping("/classes")
    public String classes(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("fullName", userDetails.getUser().getFullName());
        model.addAttribute("enrollments", studentService.getJoinedClasses(userDetails.getUser()));
        return "student/classes/list";
    }

    @GetMapping("/classes/{classId}/learning")
    public String learning(@PathVariable Integer classId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("classId", classId);
            model.addAttribute("roadmaps", learningContentService.getStudentRoadmaps(classId, userDetails.getUser()));
            return "student/classes/learning";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/classes";
        }
    }

    @GetMapping("/lessons/{id}")
    public String lesson(@PathVariable Integer id,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("lesson", learningContentService.getStudentLesson(id, userDetails.getUser()));
            model.addAttribute("materials", learningContentService.getStudentLessonMaterials(id, userDetails.getUser()));
            model.addAttribute("quizzes", quizService.getStudentQuizzesByLesson(id, userDetails.getUser()));
            return "student/classes/lesson-learning";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/classes";
        }
    }

    @GetMapping("/materials/{id}")
    public String material(@PathVariable Integer id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("material", learningContentService.getStudentMaterial(id, userDetails.getUser()));
            return "student/classes/material-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/classes";
        }
    }

    @PostMapping("/materials/{id}/mark-viewed")
    public String markViewed(@PathVariable Integer id,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        try {
            learningContentService.markMaterialViewed(id, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Da danh dau material da xem.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/student/materials/" + id;
    }

    @GetMapping("/learning-history")
    public String learningHistory(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("history", learningContentService.getLearningHistory(userDetails.getUser()));
        return "student/classes/learning-history";
    }
}
