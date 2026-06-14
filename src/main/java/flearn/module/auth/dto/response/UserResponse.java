package flearn.module.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private Integer role;
    private String roleName;
    private String status;
    private String department;
    private Boolean isActive;
    private Date createdAt;
}
