package flearn.controller;

import flearn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin-dashboard"; // Gọi file giao diện Admin
    }

    @PostMapping("/create-teacher")
    public String createTeacher(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String fullName,
                                @RequestParam String email,
                                Model model) {
        try {
            userService.createTeacher(username, password, fullName, email);
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toggle-user")
    public String toggleUserStatus(@RequestParam Integer userId) {
        userService.toggleUserStatus(userId);
        return "redirect:/admin/dashboard";
    }
}