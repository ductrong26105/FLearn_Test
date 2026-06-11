package flearn.mapper;

import flearn.dto.response.QuizResultResponse;
import flearn.entity.QuizResult;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {QuizMapper.class, UserMapper.class}, nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface QuizResultMapper {
    QuizResultResponse toResponse(QuizResult result);

    List<QuizResultResponse> toResponseList(List<QuizResult> results);
}
