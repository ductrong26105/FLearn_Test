package flearn.module.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStudentRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống.")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 6, max = 255, message = "Mật khẩu phải có ít nhất 6 ký tự.")
    private String password;

    @NotBlank(message = "Họ tên không được để trống.")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự.")
    private String fullName;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không đúng định dạng.")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự.")
    private String email;
}
