package com.smart.tailor.service;


import com.smart.tailor.entities.User;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.request.UserUpdateRequest;
import com.smart.tailor.utils.response.GrowthPercentageResponse;
import com.smart.tailor.utils.response.UserResponse;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<User> getUserDetailByEmail(String email);

    User getUserByPhoneNumber(String phoneNumber);

    User getUserByEmail(String email);

    void saveOrUpdateUser(User user);

    UserResponse convertToUserResponse(User user);

    User registerNewUsers(UserRequest userRequest);

    List<UserResponse> getAllUserResponse();

    Boolean updateStatusAccount(String email, UserStatus userStatus);

    User updateUserProfile(UserRequest userRequest);

    UserResponse updateUserProfile(String userID, UserUpdateRequest userUpdateRequest) throws Exception;

    Optional<User> getUserByUserID(String String);

    List<UserResponse> findAllUserByRoleName(RoleType roleType);

    List<User> findAllUnverifiedUser();

    void deleteUnverifiedUser(String userID);

    GrowthPercentageResponse calculateUserGrowthPercentageForCurrentAndPreviousWeek();

    GrowthPercentageResponse calculateNewCustomerGrowthPercentageForCurrentAndPreviousWeek();

    GrowthPercentageResponse calculateNewUserGrowthPercentageForCurrentAndPreviousDayByRole(String roleName);

    List<Pair<String, Long>> calculateTotalOfUser();
}
