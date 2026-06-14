package flearn.module.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Integer id;
    private String courseCode;
    private String courseName;
    private String description;
    private String status;
    private Date createdAt;
    private Date updatedAt;
}
