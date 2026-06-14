package flearn.module.content.dto.response;

import flearn.enums.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer lessonId;
    private Integer roadmapId;
    private Integer classId;
    private MaterialType type;
    private String filePath;
    private String externalUrl;
    private Boolean published;
    private Date createdAt;
    private Date updatedAt;
    private Boolean viewed;
    /** URL đã chuyển sang dạng embed (nếu là YouTube/Drive); null nếu không thể nhúng. */
    private String embedUrl;
}
