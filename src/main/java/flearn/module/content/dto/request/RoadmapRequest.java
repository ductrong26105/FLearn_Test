package flearn.module.content.dto.request;

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
public class RoadmapRequest {
    @NotBlank(message = "Ten roadmap khong duoc de trong.")
    @Size(max = 200, message = "Ten roadmap khong duoc vuot qua 200 ky tu.")
    private String title;

    @Size(max = 1000, message = "Mo ta khong duoc vuot qua 1000 ky tu.")
    private String description;
}
