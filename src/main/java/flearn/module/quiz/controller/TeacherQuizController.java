package flearn.module.quiz.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.quiz.dto.request.QuestionRequest;
import flearn.module.quiz.dto.request.QuizRequest;
import flearn.module.quiz.dto.response.QuizResponse;
import flearn.enums.QuestionType;
import flearn.module.content.service.LearningContentService;
import flearn.module.quiz.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherQuizController {
    private final QuizService quizService;
    private final LearningContentService learningContentService;

    @GetMapping("/lessons/{lessonId}/quizzes")
    public String quizzes(@PathVariable Integer lessonId,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model) {
        model.addAttribute("lesson", learningContentService.getTeacherLesson(lessonId, userDetails.getUser()));
        model.addAttribute("quizzes", quizService.getTeacherQuizzesByLesson(lessonId, userDetails.getUser()));
        return "teacher/quizzes/list";
    }

    @GetMapping("/lessons/{lessonId}/quizzes/create")
    public String createForm(@PathVariable Integer lessonId,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {
        model.addAttribute("lesson", learningContentService.getTeacherLesson(lessonId, userDetails.getUser()));
        model.addAttribute("quizRequest", QuizRequest.builder()
                .maxAttempts(1)
                .shuffleAnswers(false)
                .shuffleQuestions(false)
                .build());
        model.addAttribute("formAction", "/teacher/lessons/" + lessonId + "/quizzes/create");
        model.addAttribute("pageTitle", "Tạo quiz");
        return "teacher/quizzes/form";
    }

    @PostMapping("/lessons/{lessonId}/quizzes/create")
    public String create(@PathVariable Integer lessonId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute QuizRequest request,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/teacher/lessons/" + lessonId + "/quizzes/create";
        }
        try {
            quizService.createQuiz(lessonId, userDetails.getUser(), request);
            redirectAttributes.addFlashAttribute("successMsg", "Đã tạo quiz.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/lessons/" + lessonId + "/quizzes";
    }

    @GetMapping("/quizzes/{quizId}/edit")
    public String editForm(@PathVariable Integer quizId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {
        QuizResponse quiz = quizService.getTeacherQuiz(quizId, userDetails.getUser());
        model.addAttribute("quiz", quiz);
        model.addAttribute("quizRequest", QuizRequest.builder()
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .timeLimitMinutes(quiz.getTimeLimitMinutes())
                .deadline(quiz.getDeadline())
                .shuffleQuestions(quiz.getShuffleQuestions())
                .shuffleAnswers(quiz.getShuffleAnswers())
                .maxAttempts(quiz.getMaxAttempts())
                .videoTimestamp(quiz.getVideoTimestamp())
                .build());
        model.addAttribute("questionRequest", QuestionRequest.builder()
                .type(QuestionType.MULTIPLE_CHOICE)
                .orderIndex(0)
                .build());
        model.addAttribute("questionTypes", QuestionType.values());
        model.addAttribute("formAction", "/teacher/quizzes/" + quizId + "/edit");
        model.addAttribute("pageTitle", "Cấu hình quiz");
        return "teacher/quizzes/form";
    }

    @PostMapping("/quizzes/{quizId}/edit")
    public String update(@PathVariable Integer quizId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute QuizRequest request,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/teacher/quizzes/" + quizId + "/edit";
        }
        try {
            quizService.updateQuiz(quizId, userDetails.getUser(), request);
            redirectAttributes.addFlashAttribute("successMsg", "Đã cập nhật quiz.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/quizzes/" + quizId + "/edit";
    }

    @PostMapping("/quizzes/{quizId}/publish")
    public String publish(@PathVariable Integer quizId,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          RedirectAttributes redirectAttributes) {
        try {
            quizService.togglePublish(quizId, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Đã đổi trạng thái publish quiz.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/quizzes/" + quizId + "/edit";
    }

    @PostMapping("/quizzes/{quizId}/delete")
    public String delete(@PathVariable Integer quizId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         RedirectAttributes redirectAttributes) {
        QuizResponse quiz = quizService.getTeacherQuiz(quizId, userDetails.getUser());
        Integer lessonId = quiz.getLessonId();
        try {
            quizService.deleteQuiz(quizId, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Đã xóa quiz.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/lessons/" + lessonId + "/quizzes";
    }

    @PostMapping("/quizzes/{quizId}/questions/create")
    public String createQuestion(@PathVariable Integer quizId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 @Valid @ModelAttribute QuestionRequest request,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/teacher/quizzes/" + quizId + "/edit";
        }
        try {
            quizService.createQuestion(quizId, userDetails.getUser(), request);
            redirectAttributes.addFlashAttribute("successMsg", "Đã thêm câu hỏi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/quizzes/" + quizId + "/edit";
    }

    @GetMapping("/questions/{questionId}/edit")
    public String editQuestionForm(@PathVariable Integer questionId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {
        Integer quizId = quizService.getQuestionQuizId(questionId, userDetails.getUser());
        model.addAttribute("quizId", quizId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("questionRequest", quizService.getQuestionForEdit(questionId, userDetails.getUser()));
        model.addAttribute("questionTypes", QuestionType.values());
        return "teacher/quizzes/question-form";
    }

    @PostMapping("/questions/{questionId}/edit")
    public String updateQuestion(@PathVariable Integer questionId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 @Valid @ModelAttribute QuestionRequest request,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        Integer quizId = quizService.getQuestionQuizId(questionId, userDetails.getUser());
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/teacher/questions/" + questionId + "/edit";
        }
        try {
            quizService.updateQuestion(questionId, userDetails.getUser(), request);
            redirectAttributes.addFlashAttribute("successMsg", "Đã cập nhật câu hỏi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/quizzes/" + quizId + "/edit";
    }

    @PostMapping("/questions/{questionId}/delete")
    public String deleteQuestion(@PathVariable Integer questionId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        Integer quizId = quizService.getQuestionQuizId(questionId, userDetails.getUser());
        try {
            quizService.deleteQuestion(questionId, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Đã xóa câu hỏi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/quizzes/" + quizId + "/edit";
    }

    @GetMapping("/quizzes/{quizId}/results")
    public String results(@PathVariable Integer quizId,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          Model model) {
        model.addAttribute("quiz", quizService.getTeacherQuiz(quizId, userDetails.getUser()));
        model.addAttribute("results", quizService.getTeacherQuizResults(quizId, userDetails.getUser()));
        return "teacher/quizzes/results";
    }
}
