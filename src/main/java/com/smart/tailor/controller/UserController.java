package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.request.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.UserAPI.USER)
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final JwtService jwtService;

    @GetMapping(APIConstant.UserAPI.GET_ALL_CUSTOMER)
    public ResponseEntity<ObjectNode> getAllCustomer() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var customerList = userService.findAllUserByRoleName(RoleType.CUSTOMER);
        if (!customerList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_CUSTOMER_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(customerList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_CUSTOMER);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.GET_ALL_BRAND)
    public ResponseEntity<ObjectNode> getAllBrand() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var brandList = userService.findAllUserByRoleName(RoleType.BRAND);
        if (!brandList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_BRAND_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_BRAND);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.GET_ALL_ACCOUNTANT)
    public ResponseEntity<ObjectNode> getAllAccountant() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var accountantList = userService.findAllUserByRoleName(RoleType.ACCOUNTANT);
        if (!accountantList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_ACCOUNTANT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(accountantList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_ACCOUNTANT);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.GET_ALL_EMPLOYEE)
    public ResponseEntity<ObjectNode> getAllEmployee() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var employeeList = userService.findAllUserByRoleName(RoleType.EMPLOYEE);
        if (!employeeList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_EMPLOYEE_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(employeeList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_EMPLOYEE);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.GET_ALL_MANAGER)
    public ResponseEntity<ObjectNode> getAllManager() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var managerList = userService.findAllUserByRoleName(RoleType.MANAGER);
        if (!managerList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_MANAGER_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(managerList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_MANAGER);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.GET_ALL_ADMIN)
    public ResponseEntity<ObjectNode> getAllAdmin() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var managerList = userService.findAllUserByRoleName(RoleType.ADMIN);
        if (!managerList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_ADMIN_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(managerList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_ADMIN);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.CALCULATE_USER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK)
    public ResponseEntity<ObjectNode> calculateUserGrowthPercentageForCurrentAndPreviousWeek() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var growthResponse = userService.calculateUserGrowthPercentageForCurrentAndPreviousWeek();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate User Growth Percentage For Current and Previous Week Successfully");
        response.set("data", objectMapper.valueToTree(growthResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.CALCULATE_NEW_CUSTOMER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK)
    public ResponseEntity<ObjectNode> calculateNewCustomerGrowthPercentageForCurrentAndPreviousWeek() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var growthResponse = userService.calculateNewCustomerGrowthPercentageForCurrentAndPreviousWeek();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate New Customer Growth Percentage For Current and Previous Week Successfully");
        response.set("data", objectMapper.valueToTree(growthResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.CALCULATE_NEW_USER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_DAY_BY_ROLE_NAME + "/{roleName}")
    public ResponseEntity<ObjectNode> calculateNewUserGrowthPercentageForCurrentAndPreviousDayByRole(@PathVariable("roleName") String roleName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var growthResponse = userService.calculateNewUserGrowthPercentageForCurrentAndPreviousDayByRole(roleName);
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate New User Growth Percentage For Current and Previous Day By Role Successfully");
        response.set("data", objectMapper.valueToTree(growthResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(APIConstant.UserAPI.CALCULATE_TOTAL_OF_USER)
    public ResponseEntity<ObjectNode> calculateTotalOfUserByRoleName() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var totalUserResponse = userService.calculateTotalOfUser();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate Total Of User Successfully");
        response.set("data", objectMapper.valueToTree(totalUserResponse));
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.UserAPI.UPDATE_USER + "/{userID}")
    public ResponseEntity<ObjectNode> updateUser(@PathVariable("userID") String userID,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                 @Valid @RequestBody UserUpdateRequest userUpdateRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if (!userID.equals(userIDFromJwtToken)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        var totalUserResponse = userService.updateUserProfile(userID, userUpdateRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Update User Successfully");
        response.set("data", objectMapper.valueToTree(totalUserResponse));
        return ResponseEntity.ok(response);
    }
}
