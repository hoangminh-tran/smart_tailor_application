package com.smart.tailor.mapper;

import com.smart.tailor.entities.Customer;
import com.smart.tailor.utils.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(source = "customer.user.email", target = "email")
    @Mapping(source = "customer.user.fullName", target = "fullName")
    @Mapping(source = "customer.user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "customer.user.userStatus", target = "userStatus")
    @Mapping(source = "customer.user.roles.roleName", target = "roleName")
    @Mapping(source = "customer.user.imageUrl", target = "imageUrl")
    CustomerResponse mapperToCustomerResponse(Customer customer);
}
