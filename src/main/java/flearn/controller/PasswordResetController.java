package flearn.controller;

import flearn.entity.User;
import flearn.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // === GIAO DIỆN NHẬP EMAIL YÊU CẦU ĐỔI PASS ===
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            passwordResetService.processForgotPassword(email);
            model.addAttribute("successMsg", "Hệ thống đã gửi hướng dẫn đặt lại mật khẩu vào hòm thư: " + email);
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "forgot-password";
    }

    // === GIAO DIỆN NHẬP MẬT KHẨU MỚI (Từ link trong Email) ===
    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        try {
            // Kiểm tra token có chuẩn và còn hạn không
            passwordResetService.getByResetToken(token);
            model.addAttribute("token", token);
            return "reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "forgot-password"; // Nếu token lỗi, đẩy về trang nhập email
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String newPassword,
                                       Model model) {
        try {
            User user = passwordResetService.getByResetToken(token);
            passwordResetService.updatePassword(user, newPassword);
            // Cập nhật thành công, chuyển hướng về trang đăng nhập
            return "redirect:/login?resetSuccess=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("token", token);
            return "reset-password";
        }
    }
}