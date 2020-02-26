package com.cognizant.feedback.mapper;

import com.cognizant.feedback.dto.UserDTO;
import com.cognizant.feedback.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roleName", source = "userEntity.userRoleDetails.roleName")
    @Mapping(target = "businessUnit", source = "userEntity.businessUnit.buName")
    @Mapping(target = "password", constant ="")
    UserDTO userEntityToUserDTO(UserEntity userEntity);

    List<UserDTO> userEntitiesToUserDTOs(List<UserEntity> userEntities);

}
