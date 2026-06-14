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
public class UpdateUserRequest {
    @NotBlank(message = "Ho ten khong duoc de trong.")
    @Size(max = 100, message = "Ho ten khong duoc vuot qua 100 ky tu.")
    private String fullName;

    @NotBlank(message = "Email khong duoc de trong.")
    @Email(message = "Email khong dung dinh dang.")
    @Size(max = 100, message = "Email khong duoc vuot qua 100 ky tu.")
    private String email;

    @Size(max = 20, message = "So dien thoai khong duoc vuot qua 20 ky tu.")
    private String phone;

    @Size(max = 100, message = "Phong ban khong duoc vuot qua 100 ky tu.")
    private String department;
}
