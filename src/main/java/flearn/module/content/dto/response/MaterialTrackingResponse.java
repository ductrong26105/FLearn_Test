package flearn.module.content.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialTrackingResponse {
    private Integer id;
    private MaterialResponse material;
    private Boolean viewed;
    private Date viewedAt;
}
