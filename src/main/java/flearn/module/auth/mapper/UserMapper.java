package flearn.module.auth.mapper;

import flearn.module.auth.dto.response.UserResponse;
import flearn.enums.Role;
import flearn.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {
    @Mapping(target = "roleName", expression = "java(roleName(user))")
    @Mapping(target = "status", expression = "java(user.getStatus() == null ? null : user.getStatus().name())")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    default String roleName(User user) {
        return user == null ? null : Role.fromCode(user.getRole()).name();
    }
}
