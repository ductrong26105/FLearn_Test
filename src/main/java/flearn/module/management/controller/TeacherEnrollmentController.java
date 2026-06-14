package flearn.module.management.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.management.dto.request.RejectEnrollmentRequest;
import flearn.module.management.service.ClassroomService;
import flearn.module.management.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teacher/classes")
@RequiredArgsConstructor
public class TeacherEnrollmentController {
    private final ClassroomService classroomService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/{id}/enrollments")
    public String enrollments(@PathVariable Integer id,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model) {
        model.addAttribute("classroom", classroomService.getTeacherClassById(id, userDetails.getUser()));
        model.addAttribute("enrollments", enrollmentService.getClassEnrollments(id, userDetails.getUser()));
        model.addAttribute("rejectEnrollmentRequest", new RejectEnrollmentRequest());
        return "teacher/classes/enrollments";
    }

    @PostMapping("/{classId}/enrollments/{enrollmentId}/approve")
    public String approve(@PathVariable Integer classId,
                          @PathVariable Integer enrollmentId,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.approveEnrollment(classId, enrollmentId, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Da approve yeu cau vao lop.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/classes/" + classId + "/enrollments";
    }

    @PostMapping("/{classId}/enrollments/{enrollmentId}/reject")
    public String reject(@PathVariable Integer classId,
                         @PathVariable Integer enrollmentId,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         @Valid @ModelAttribute RejectEnrollmentRequest request,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/teacher/classes/" + classId + "/enrollments";
        }
        try {
            enrollmentService.rejectEnrollment(classId, enrollmentId, userDetails.getUser(), request);
            redirectAttributes.addFlashAttribute("successMsg", "Da reject yeu cau vao lop.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/classes/" + classId + "/enrollments";
    }

    @PostMapping("/{classId}/students/{studentId}/remove")
    public String removeStudent(@PathVariable Integer classId,
                                @PathVariable Integer studentId,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.removeStudent(classId, studentId, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Da dua student ra khoi lop.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/teacher/classes/" + classId;
    }
}
