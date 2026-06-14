package flearn.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException exception,
                                          HttpServletRequest request,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMsg", exception.getMessage());
        return redirectBack(request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(ConstraintViolationException exception,
                                            HttpServletRequest request,
                                            RedirectAttributes redirectAttributes) {
        String message = exception.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Du lieu khong hop le.");
        redirectAttributes.addFlashAttribute("errorMsg", message);
        return redirectBack(request);
    }

    private String redirectBack(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer == null ? "/dashboard" : referer);
    }
}
