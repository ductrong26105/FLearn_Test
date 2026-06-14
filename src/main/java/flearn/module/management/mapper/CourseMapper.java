package flearn.module.management.mapper;

import flearn.module.management.dto.response.CourseResponse;
import flearn.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CourseMapper {
    @Mapping(target = "status", expression = "java(course.getStatus() == null ? null : course.getStatus().name())")
    CourseResponse toResponse(Course course);

    List<CourseResponse> toResponseList(List<Course> courses);
}
