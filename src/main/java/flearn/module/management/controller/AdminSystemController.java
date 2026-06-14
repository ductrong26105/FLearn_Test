package flearn.module.management.controller;

import flearn.module.management.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/system")
@RequiredArgsConstructor
public class AdminSystemController {
    private final SystemSettingService systemSettingService;

    @GetMapping("/settings")
    public String systemSettings(Model model) {
        model.addAttribute("maintenanceMode", systemSettingService.isMaintenanceMode());
        return "admin/system/settings";
    }

    @PostMapping("/settings/maintenance")
    public String updateMaintenance(@RequestParam(defaultValue = "false") boolean enabled,
                                    RedirectAttributes redirectAttributes) {
        systemSettingService.setMaintenanceMode(enabled);
        redirectAttributes.addFlashAttribute("successMsg", enabled ? "Da bat che do bao tri." : "Da tat che do bao tri.");
        return "redirect:/admin/system/settings";
    }
}
