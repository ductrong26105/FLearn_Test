package flearn.module.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatisticsResponse {
    private long totalUsers;
    private long students;
    private long teachers;
    private long classes;
    private long activeClasses;
    private long blockedTeachers;
    private long newUsers;
}
