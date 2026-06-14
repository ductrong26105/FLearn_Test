package flearn.module.quiz.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.quiz.dto.response.QuizResultResponse;
import flearn.module.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentQuizController {
    private final QuizService quizService;

    @GetMapping("/quizzes/{quizId}")
    public String start(@PathVariable Integer quizId,
                        @AuthenticationPrincipal CustomUserDetails userDetails,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("quiz", quizService.startQuiz(quizId, userDetails.getUser()));
            model.addAttribute("startedAt", System.currentTimeMillis());
            return "student/quizzes/take";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/classes";
        }
    }

    @PostMapping("/quizzes/{quizId}/submit")
    public String submit(@PathVariable Integer quizId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         @RequestParam Map<String, String> answers,
                         RedirectAttributes redirectAttributes) {
        try {
            QuizResultResponse result = quizService.submitQuiz(quizId, userDetails.getUser(), answers);
            redirectAttributes.addFlashAttribute("successMsg", "Đã nộp bài. Điểm của bạn: " + result.getScore());
            return "redirect:/student/quizzes/" + quizId + "/results";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/quizzes/" + quizId;
        }
    }

    /**
     * AJAX endpoint dùng cho in-video quiz gate overlay.
     * Trả về JSON thay vì redirect, cho phép JS xử lý kết quả và resume video.
     */
    @PostMapping("/quizzes/{quizId}/submit-gate")
    @ResponseBody
    public QuizResultResponse submitGate(@PathVariable Integer quizId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam Map<String, String> answers) {
        return quizService.submitQuiz(quizId, userDetails.getUser(), answers);
    }

    @GetMapping("/quizzes/{quizId}/results")
    public String results(@PathVariable Integer quizId,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model) {
        model.addAttribute("attempts", quizService.getStudentQuizAttempts(quizId, userDetails.getUser()));
        return "student/quizzes/results";
    }

    @GetMapping("/quiz-history")
    public String history(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("history", quizService.getStudentQuizHistory(userDetails.getUser()));
        return "student/quizzes/history";
    }
}
