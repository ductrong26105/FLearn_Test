package flearn.module.management.controller;

import flearn.module.auth.dto.request.UpdateUserRequest;
import flearn.enums.Role;
import flearn.module.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {
    private final UserService userService;

    @GetMapping
    public String students(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("students", userService.searchUsersByRole(Role.STUDENT, keyword));
        model.addAttribute("keyword", keyword);
        return "admin/students/list";
    }

    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Integer id,
                                @Valid @ModelAttribute UpdateUserRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
            return "redirect:/admin/students";
        }
        try {
            userService.updateUser(id, request);
            redirectAttributes.addFlashAttribute("successMsg", "Da cap nhat student.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/students";
    }

    @PostMapping("/{id}/block")
    public String blockStudent(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.blockUser(id);
            redirectAttributes.addFlashAttribute("successMsg", "Da block student.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/students";
    }

    @PostMapping("/{id}/unblock")
    public String unblockStudent(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.unblockUser(id);
            redirectAttributes.addFlashAttribute("successMsg", "Da unblock student.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/students";
    }
}
