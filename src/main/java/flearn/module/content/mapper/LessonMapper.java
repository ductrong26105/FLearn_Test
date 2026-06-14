package flearn.module.content.mapper;

import flearn.module.content.dto.response.LessonResponse;
import flearn.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface LessonMapper {
    @Mapping(target = "description", source = "content")
    @Mapping(target = "roadmapId", source = "roadmap.id")
    @Mapping(target = "classId", source = "classroom.classId")
    @Mapping(target = "embedVideoUrl", ignore = true)
    LessonResponse toResponse(Lesson lesson);

    List<LessonResponse> toResponseList(List<Lesson> lessons);
}
