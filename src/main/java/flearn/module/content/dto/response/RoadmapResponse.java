package flearn.module.content.dto.response;

import flearn.module.management.dto.response.ClassroomResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapResponse {
    private Integer id;
    private String title;
    private String description;
    private ClassroomResponse classRoom;
    private Boolean published;
    private Date createdAt;
    private Date updatedAt;
    private List<LessonResponse> lessons;
}
