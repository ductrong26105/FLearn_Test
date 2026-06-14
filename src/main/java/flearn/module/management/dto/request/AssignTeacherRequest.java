package flearn.module.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignTeacherRequest {
    @NotNull(message = "Vui long chon giao vien.")
    private Integer teacherId;
}
