package flearn.module.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        // Kiểm tra quyền của người dùng đang đăng nhập
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard"; // Admin về trang Admin
            } else if (auth.getAuthority().equals("ROLE_TEACHER")) {
                return "redirect:/teacher/dashboard"; // Giáo viên về trang Giáo viên
            }
        }

        return "redirect:/student/dashboard";
    }

    @GetMapping("/maintenance")
    public String maintenance() {
        return "error/maintenance";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}
