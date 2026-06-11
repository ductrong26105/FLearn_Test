package flearn.mapper;

import flearn.dto.response.ClassroomResponse;
import flearn.entity.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface ClassroomMapper {
    @Mapping(target = "classCode", source = "classCode")
    @Mapping(target = "status", expression = "java(classroom.getStatus() == null ? null : classroom.getStatus().name())")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseName", source = "course.courseName")
    @Mapping(target = "courseCode", source = "course.courseCode")
    ClassroomResponse toResponse(Classroom classroom);

    List<ClassroomResponse> toResponseList(List<Classroom> classrooms);
}
