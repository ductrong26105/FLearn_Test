package flearn.module.management.dto.request;

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
public class RejectEnrollmentRequest {
    @NotBlank(message = "Vui long nhap ly do tu choi.")
    @Size(max = 500, message = "Ly do tu choi khong duoc vuot qua 500 ky tu.")
    private String rejectReason;
}
