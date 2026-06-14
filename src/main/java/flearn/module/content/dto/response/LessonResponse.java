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
public class LessonResponse {
    private Integer lessonId;
    private String title;
    private String content;
    private String description;
    private String videoUrl;
    /** URL video lesson đã chuyển sang dạng embed (YouTube/Drive); null nếu không đặt hoặc không nhập được. */
    private String embedVideoUrl;
    private Integer roadmapId;
    private Integer classId;
    private Integer orderIndex;
    private Boolean visible;
    private Date createdAt;
    private Date updatedAt;
}
