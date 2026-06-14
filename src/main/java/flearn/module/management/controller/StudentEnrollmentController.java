package flearn.module.management.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.management.dto.request.JoinClassRequest;
import flearn.module.management.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/student/classes")
@RequiredArgsConstructor
public class StudentEnrollmentController {
    private final StudentService studentService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinClassRequest", new JoinClassRequest());
        return "student/classes/join";
    }

    @PostMapping("/join")
    public String join(@AuthenticationPrincipal CustomUserDetails userDetails,
                       @Valid @ModelAttribute JoinClassRequest request,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/student/classes/join";
        }
        try {
            studentService.joinClass(request, userDetails.getUser());
            redirectAttributes.addFlashAttribute("successMsg", "Da gui yeu cau tham gia lop. Vui long cho teacher duyet.");
            return "redirect:/student/classes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/student/classes/join";
        }
    }
}
