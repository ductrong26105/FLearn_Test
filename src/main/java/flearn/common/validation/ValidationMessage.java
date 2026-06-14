package flearn.common.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public final class ValidationMessage {
    private ValidationMessage() {
    }

    public static String firstError(BindingResult bindingResult) {
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError == null || fieldError.getDefaultMessage() == null) {
            return "Du lieu khong hop le.";
        }
        return fieldError.getDefaultMessage();
    }
}
