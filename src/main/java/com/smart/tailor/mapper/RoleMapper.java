package com.smart.tailor.mapper;

import com.smart.tailor.entities.Roles;
import com.smart.tailor.utils.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse mapperToRoleResponse(Roles role);
}
