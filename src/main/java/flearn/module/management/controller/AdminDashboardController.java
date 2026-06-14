package flearn.module.management.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.management.dto.request.AssignTeacherRequest;
import flearn.enums.Role;
import flearn.module.management.service.ClassroomService;
import flearn.module.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final UserService userService;
    private final ClassroomService classroomService;

    @GetMapping({"", "/dashboard"})
    public String adminDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("fullName", userDetails.getUser().getFullName());
        model.addAttribute("statistics", classroomService.getAdminStatistics());
        model.addAttribute("classes", classroomService.getAllClasses());
        model.addAttribute("teachers", userService.getUsersByRole(Role.TEACHER));
        model.addAttribute("assignTeacherRequest", new AssignTeacherRequest());
        return "admin/dashboard";
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        model.addAttribute("statistics", classroomService.getAdminStatistics());
        return "admin/statistics";
    }
}
