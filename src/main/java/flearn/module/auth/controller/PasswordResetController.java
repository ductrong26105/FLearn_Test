package flearn.module.auth.controller;

import flearn.module.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    // =====================================================
    // BƯỚC 1: Nhập email → gửi OTP
    // =====================================================

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                        RedirectAttributes redirectAttributes) {
        try {
            passwordResetService.sendOtp(email.trim());
            redirectAttributes.addFlashAttribute("email", email.trim());
            return "redirect:/reset-password";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/forgot-password";
        }
    }

    // =====================================================
    // BƯỚC 2: Nhập OTP + mật khẩu mới (gộp chung 1 trang)
    // =====================================================

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        if (model.getAttribute("email") == null) {
            return "redirect:/forgot-password";
        }
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String email,
                                       @RequestParam String otpCode,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       RedirectAttributes redirectAttributes) {
        // Validate client-side confirm password
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMsg", "Xác nhận mật khẩu không khớp.");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/reset-password";
        }
        try {
            passwordResetService.resetPasswordWithOtp(email.trim(), otpCode.trim(), newPassword);
            redirectAttributes.addFlashAttribute("successMsg", "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập ngay.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/reset-password";
        }
    }

    // =====================================================
    // /verify-otp — giữ route cũ để không bị 404
    // =====================================================
    @GetMapping("/verify-otp")
    public String redirectVerifyOtp() {
        return "redirect:/forgot-password";
    }
}
