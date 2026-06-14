package flearn.module.content.dto.request;

import flearn.enums.MaterialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {
    @NotBlank(message = "Ten material khong duoc de trong.")
    @Size(max = 200, message = "Ten material khong duoc vuot qua 200 ky tu.")
    private String title;

    @Size(max = 1000, message = "Mo ta khong duoc vuot qua 1000 ky tu.")
    private String description;

    @NotNull(message = "Vui long chon loai material.")
    private MaterialType type;

    @Size(max = 1000, message = "Link khong duoc vuot qua 1000 ky tu.")
    private String externalUrl;

    private MultipartFile file;
}
