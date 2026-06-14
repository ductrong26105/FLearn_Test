package flearn.module.content.mapper;

import flearn.module.content.dto.response.MaterialTrackingResponse;
import flearn.entity.MaterialTracking;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = MaterialMapper.class, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface MaterialTrackingMapper {
    MaterialTrackingResponse toResponse(MaterialTracking tracking);

    List<MaterialTrackingResponse> toResponseList(List<MaterialTracking> trackings);
}
