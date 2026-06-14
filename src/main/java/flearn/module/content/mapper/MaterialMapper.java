package flearn.module.content.mapper;

import flearn.module.content.dto.response.MaterialResponse;
import flearn.entity.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface MaterialMapper {
    @Mapping(target = "lessonId", source = "lesson.lessonId")
    @Mapping(target = "roadmapId", source = "lesson.roadmap.id")
    @Mapping(target = "classId", source = "lesson.classroom.classId")
    @Mapping(target = "viewed", ignore = true)
    @Mapping(target = "embedUrl", ignore = true)
    MaterialResponse toResponse(Material material);

    List<MaterialResponse> toResponseList(List<Material> materials);
}
