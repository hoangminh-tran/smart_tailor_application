package com.smart.tailor.mapper;

import com.smart.tailor.entities.User;
import com.smart.tailor.utils.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "roles.roleName", target = "roleName")
    @Mapping(source = "user.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "user.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    UserResponse mapperToUserResponse(User user);
}
