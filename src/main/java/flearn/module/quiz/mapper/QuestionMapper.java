package flearn.module.quiz.mapper;

import flearn.module.quiz.dto.response.QuestionResponse;
import flearn.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface QuestionMapper {
    @Mapping(target = "answers", ignore = true)
    QuestionResponse toResponse(Question question);

    List<QuestionResponse> toResponseList(List<Question> questions);
}
