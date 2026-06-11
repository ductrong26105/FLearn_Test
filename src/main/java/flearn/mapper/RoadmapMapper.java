package flearn.mapper;

import flearn.dto.response.RoadmapResponse;
import flearn.entity.Roadmap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ClassroomMapper.class}, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface RoadmapMapper {
    @Mapping(target = "lessons", ignore = true)
    RoadmapResponse toResponse(Roadmap roadmap);

    List<RoadmapResponse> toResponseList(List<Roadmap> roadmaps);
}
