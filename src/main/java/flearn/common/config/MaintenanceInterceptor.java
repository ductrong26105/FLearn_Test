package flearn.common.config;

import flearn.enums.Role;
import flearn.module.management.service.SystemSettingService;
import flearn.security.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor

// Interceptor này sẽ kiểm tra nếu hệ thống đang ở chế độ bảo trì,
// nó sẽ chặn tất cả các yêu cầu trừ những yêu cầu đến trang bảo trì, trang đăng nhập, trang đăng xuất và các tài nguyên tĩnh.
// Tuy nhiên, nếu người dùng đã đăng nhập và có vai trò ADMIN, họ vẫn có thể truy cập bình thường.
public class MaintenanceInterceptor implements HandlerInterceptor {
    private final SystemSettingService systemSettingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!systemSettingService.isMaintenanceMode() || isAllowedPath(request.getRequestURI())) {
            return true;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails
                && userDetails.getUser().getRoleType() == Role.ADMIN) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/maintenance");
        return false;
    }

    private boolean isAllowedPath(String uri) {
        return uri.equals("/maintenance")
                || uri.equals("/login")
                || uri.equals("/logout")
                || uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/images/")
                || uri.startsWith("/forgot-password")
                || uri.startsWith("/verify-otp")
                || uri.startsWith("/reset-password");
    }
}
