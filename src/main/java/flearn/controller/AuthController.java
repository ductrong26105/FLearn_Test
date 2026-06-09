package flearn.controller;

import flearn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // Cập nhật hàm này để hiện thông báo thay vì chuyển trang luôn
    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String fullName,
                                  @RequestParam String email,
                                  Model model) {
        try {
            authService.registerStudent(username, password, fullName, email);
            model.addAttribute("successMsg", "Đăng ký thành công! Vui lòng kiểm tra hộp thư Email để kích hoạt tài khoản.");
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "register"; // Ở lại trang đăng ký để người dùng đọc được thông báo
    }

    // Thêm hàm này để xử lý khi người dùng click link trong Email
    @GetMapping("/verify-account")
    public String verifyAccount(@RequestParam String token, Model model) {
        try {
            authService.verifyAccount(token);
            model.addAttribute("successMsg", "Xác thực tài khoản thành công! Bạn đã có thể đăng nhập.");
            return "login"; // Thành công thì đá về trang đăng nhập
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "login";
        }
    }
}