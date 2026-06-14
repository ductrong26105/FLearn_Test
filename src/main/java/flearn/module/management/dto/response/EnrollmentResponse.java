package flearn.module.management.dto.response;

import flearn.module.auth.dto.response.UserResponse;
import flearn.module.management.dto.response.ClassroomResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private Integer id;
    private UserResponse student;
    private ClassroomResponse classRoom;
    private String status;
    private String requestMessage;
    private String rejectReason;
    private Date requestedAt;
    private Date approvedAt;
    private Date rejectedAt;
    private Date removedAt;
}
