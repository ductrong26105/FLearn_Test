package flearn.module.auth.controller;

import flearn.security.service.CustomUserDetails;
import flearn.module.auth.dto.request.ChangePasswordRequest;
import flearn.module.auth.service.AuthService;
import flearn.common.validation.ValidationMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AuthService authService;

    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute ChangePasswordRequest request,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", ValidationMessage.firstError(bindingResult));
            return "auth/change-password";
        }

        try {
            authService.changePassword(userDetails.getUser(), request);
            model.addAttribute("successMsg", "Doi mat khau thanh cong.");
        } catch (RuntimeException exception) {
            model.addAttribute("errorMsg", exception.getMessage());
        }
        return "auth/change-password";
    }
}
