package flearn.module.management.mapper;

import flearn.module.management.dto.response.EnrollmentResponse;
import flearn.module.auth.mapper.UserMapper;
import flearn.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ClassroomMapper.class}, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface EnrollmentMapper {
    @Mapping(target = "status", expression = "java(enrollment.getStatus() == null ? null : enrollment.getStatus().name())")
    EnrollmentResponse toResponse(Enrollment enrollment);

    List<EnrollmentResponse> toResponseList(List<Enrollment> enrollments);
}
