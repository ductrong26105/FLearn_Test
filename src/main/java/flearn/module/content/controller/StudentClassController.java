package flearn.module.content.controller;

import flearn.enums.MaterialType;
import flearn.module.content.dto.response.MaterialResponse;
import flearn.security.service.CustomUserDetails;
import flearn.module.content.service.LearningContentService;
import flearn.module.quiz.service.QuizService;
import flearn.module.management.service.StudentService;
import flearn.module.schedule.service.ClassScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import flearn.module.schedule.dto.response.ClassScheduleResponse;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentClassController {
    private final StudentService studentService;
    private final LearningContentService learningContentService;
    private final QuizService quizService;
    private final ClassScheduleService classScheduleService;

    @GetMapping({"", "/dashboard"})
    public String studentDashboard() {
        return "redirect:/student/classes";
    }

    /** Trang thời khóa biểu tổng hợp học tập. */
    @GetMapping("/schedules")
    public String studentSchedules(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<ClassScheduleResponse> schedules = classScheduleService.getSchedulesByStudent(userDetails.getUser());

        model.addAttribute("fullName", userDetails.getUser().getFullName());
        model.addAttribute("schedules", schedules);
        
        return "student/classes/schedules";
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

    /**
     * [FIX-PDF] Endpoint stream file PDF trực tiếp với đúng Content-Type và Content-Disposition: inline.
     * Giải quyết lỗi X-Frame-Options: DENY từ Spring Security khiến iframe không hiển thị được PDF.
     * Đồng thời kiểm soát quyền truy cập: chỉ sinh viên đang enrolled mới xem được file.
     */
    @GetMapping("/materials/{id}/pdf")
    public ResponseEntity<Resource> streamMaterialPdf(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // 1. Validate quyền – ném BusinessException nếu student không có quyền
            MaterialResponse material = learningContentService.getStudentMaterial(id, userDetails.getUser());

            // 2. Chỉ xử lý tài liệu loại PDF có filePath
            if (material.getType() != MaterialType.PDF
                    || material.getFilePath() == null
                    || material.getFilePath().isBlank()) {
                return ResponseEntity.notFound().build();
            }

            // 3. Resolve path file trên disk (filePath = "/uploads/materials/uuid.pdf")
            String relativePath = material.getFilePath().startsWith("/")
                    ? material.getFilePath().substring(1)
                    : material.getFilePath();
            Path filePath = Path.of(".").toAbsolutePath().resolve(relativePath).normalize();
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // 4. Serve với header inline để hiển thị trong iframe
            String filename = resource.getFilename() != null ? resource.getFilename() : "document.pdf";
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    // Override X-Frame-Options cho endpoint này: SAMEORIGIN cho phép iframe cùng domain
                    .header("X-Frame-Options", "SAMEORIGIN")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(403).build();
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
