package flearn.module.auth.controller;

import flearn.module.auth.dto.request.RegisterStudentRequest;
import flearn.module.auth.service.AuthService;
import flearn.common.validation.ValidationMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute RegisterStudentRequest request,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", ValidationMessage.firstError(bindingResult));
            return "auth/register";
        }
        try {
            authService.registerStudent(request);
            redirectAttributes.addFlashAttribute("successMsg", "Đăng ký thành công. Bạn có thể đăng nhập ngay.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "auth/register";
        }
    }
}
