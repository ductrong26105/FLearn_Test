package flearn.module.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinClassRequest {
    @NotBlank(message = "Ma tham gia khong duoc de trong.")
    @Pattern(regexp = "^[A-Za-z0-9]{6}$", message = "Ma tham gia phai gom 6 ky tu chu hoac so.")
    private String inviteCode;

    @Size(max = 500, message = "Loi nhan khong duoc vuot qua 500 ky tu.")
    private String requestMessage;
}
