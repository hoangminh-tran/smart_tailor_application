package com.smart.tailor.service.impl;


import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Roles;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.RoleType;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.UserMapper;
import com.smart.tailor.repository.UserRepository;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.service.TokenService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.service.VerificationTokenService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.request.UserUpdateRequest;
import com.smart.tailor.utils.response.GrowthPercentageResponse;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VerificationTokenService verificationTokenService;
    private final TokenService tokenService;

    @Override
    public Optional<User> getUserDetailByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User registerNewUsers(UserRequest userRequest) {
        User savedUser = null;
        Optional<Roles> role = roleService.findRoleByRoleName(userRequest.getRoleName().trim().toUpperCase());
        if (!role.isPresent()) {
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ROLE + " " + userRequest.getRoleName());
        }

        if (!userRequest.getEmail().isEmpty() && !userRequest.getEmail().isBlank() && !userRequest.getPassword().isEmpty() && !userRequest.getPassword().isBlank()) {
            savedUser = userRepository.save(
                    User
                            .builder()
                            .email(userRequest.getEmail())
                            .password(userRequest.getPassword())
                            .language(userRequest.getLanguage())
                            .provider(userRequest.getProvider())
                            .userStatus(userRequest.getProvider().equals(Provider.LOCAL) ? UserStatus.INACTIVE : UserStatus.ACTIVE)
                            .fullName(userRequest.getFullName())
                            .phoneNumber(userRequest.getPhoneNumber())
                            .roles(role.get())
                            .imageUrl(userRequest.getImageUrl())
                            .build()
            );
        }
        return savedUser;
    }

    @Override
    public List<UserResponse> getAllUserResponse() {
        return userRepository.findAll().stream().map(this::convertToUserResponse).toList();
    }

    public UserResponse convertToUserResponse(User user) {
        return userMapper.mapperToUserResponse(user);
    }

    @Override
    public Boolean updateStatusAccount(String email, UserStatus userStatus) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setUserStatus(userStatus);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public User updateUserProfile(UserRequest userRequest) {
        User userExisted = userRepository.getByEmail(userRequest.getEmail());
        if (userExisted != null) {
            userExisted.setFullName(userRequest.getFullName());
            userExisted.setImageUrl(userRequest.getImageUrl());
            userExisted.setPhoneNumber(userRequest.getPhoneNumber());
            return userRepository.save(userExisted);
        }
        return null;
    }

    @Override
    public UserResponse updateUserProfile(String userID, UserUpdateRequest userRequest) throws Exception {
        var invalidInfor = !Utilities.isStringNotNullOrEmpty(userRequest.getPhoneNumber())
                && !Utilities.isStringNotNullOrEmpty(userRequest.getFullName())
                && !Utilities.isStringNotNullOrEmpty(userRequest.getImageUrl());

        if (invalidInfor) {
            throw new BadRequestException("MISSING ARGUMENT");
        }

        var user = userRepository.findById(userID).orElseThrow(() -> {
            return new BadRequestException("CAN NOT FIND USER!");
        });

        if (Utilities.isStringNotNullOrEmpty(userRequest.getPhoneNumber())
                && Utilities.isValidVietnamesePhoneNumber(userRequest.getPhoneNumber())) {
            user.setPhoneNumber(userRequest.getPhoneNumber());
        }

        if (Utilities.isStringNotNullOrEmpty(userRequest.getFullName())) {
            user.setFullName(userRequest.getFullName());
        }

        if (Utilities.isStringNotNullOrEmpty(userRequest.getImageUrl())) {
            user.setImageUrl(userRequest.getImageUrl());
        }

        var updated = userRepository.save(user);

        return userMapper.mapperToUserResponse(updated);
    }

    @Override
    public Optional<User> getUserByUserID(String String) {
        return userRepository.findById(String);
    }

    @Override
    public List<UserResponse> findAllUserByRoleName(RoleType roleType) {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getRoles().getRoleName().equals(roleType.name()))
                .sorted(
                        Comparator
                                .comparing(User::getLastModifiedDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .reversed()
                                .thenComparing(User::getCreateDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .reversed()
                )
                .map(userMapper::mapperToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllUnverifiedUser() {
        return userRepository
                .getAllUserWithEmailUnverified(false, TypeOfVerification.VERIFY_ACCOUNT.name())
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUnverifiedUser(String userID) {
        verificationTokenService.deleteVerificationTokenByUserID(userID);
        tokenService.deleteTokenByUserID(userID);
        userRepository.deleteUserByUserID(userID);
    }

    @Override
    public GrowthPercentageResponse calculateUserGrowthPercentageForCurrentAndPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime endOfCurrentWeek = now;

        LocalDateTime startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);
        LocalDateTime endOfPreviousWeek = startOfCurrentWeek.minusSeconds(1);

        var totalUser = userRepository.findAll();

        var currentWeekUserCount = totalUser
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfCurrentWeek) && !createDate.isAfter(endOfCurrentWeek);
                })
                .count();

        var previousWeekUserCount = totalUser
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfPreviousWeek) && !createDate.isAfter(endOfPreviousWeek);
                })
                .count();

        if (previousWeekUserCount == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentWeekUserCount)
                    .previousData(previousWeekUserCount)
                    .growthPercentage(currentWeekUserCount > 0 ? 100.0f : 0.0f)
                    .build();
        }

        float growthPercentage = ((float) (currentWeekUserCount - previousWeekUserCount) / previousWeekUserCount) * 100.0f;

        var roundGrowthPercentage = BigDecimal
                .valueOf(growthPercentage)
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentWeekUserCount)
                .previousData(previousWeekUserCount)
                .growthPercentage(roundGrowthPercentage)
                .build();
    }

    @Override
    public GrowthPercentageResponse calculateNewCustomerGrowthPercentageForCurrentAndPreviousWeek() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentWeek = now.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
        LocalDateTime endOfCurrentWeek = now;

        LocalDateTime startOfPreviousWeek = startOfCurrentWeek.minusWeeks(1);
        LocalDateTime endOfPreviousWeek = startOfCurrentWeek.minusSeconds(1);

        var totalCustomer = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().getRoleName().equals(RoleType.CUSTOMER.name()))
                .toList();

        var currentWeekCustomerCount = totalCustomer
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfCurrentWeek) && !createDate.isAfter(endOfCurrentWeek);
                })
                .count();

        var previousWeekCustomerCount = totalCustomer
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfPreviousWeek) && !createDate.isAfter(endOfPreviousWeek);
                })
                .count();

        if (previousWeekCustomerCount == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentWeekCustomerCount)
                    .previousData(previousWeekCustomerCount)
                    .growthPercentage(currentWeekCustomerCount > 0 ? 100.0f : 0.0f)
                    .build();
        }

        float growthPercentage = ((float) (currentWeekCustomerCount - previousWeekCustomerCount) / previousWeekCustomerCount) * 100.0f;

        var roundGrowthPercentage = BigDecimal
                .valueOf(growthPercentage)
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentWeekCustomerCount)
                .previousData(previousWeekCustomerCount)
                .growthPercentage(roundGrowthPercentage)
                .build();
    }

    @Override
    public GrowthPercentageResponse calculateNewUserGrowthPercentageForCurrentAndPreviousDayByRole(String roleName) {
        try {
            RoleType.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("RoleName: " + roleName + " must be any of enum RoleType");
        }
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startOfCurrentDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfCurrentDay = now;

        LocalDateTime startOfPreviousDay = startOfCurrentDay.minusDays(1);
        LocalDateTime endOfPreviousDay = startOfCurrentDay.minusSeconds(1);

        var totalUserByRole = userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().getRoleName().equalsIgnoreCase(roleName))
                .toList();

        var currentDayUserCount = totalUserByRole
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfCurrentDay) && !createDate.isAfter(endOfCurrentDay);
                })
                .count();

        var previousDayUserCount = totalUserByRole
                .stream()
                .filter(user -> {
                    LocalDateTime createDate = user.getCreateDate();
                    return !createDate.isBefore(startOfPreviousDay) && !createDate.isAfter(endOfPreviousDay);
                })
                .count();

        if (previousDayUserCount == 0) {
            return GrowthPercentageResponse
                    .builder()
                    .currentData(currentDayUserCount)
                    .previousData(previousDayUserCount)
                    .growthPercentage(currentDayUserCount > 0 ? 100.0f : 0.0f)
                    .build();
        }

        float growthPercentage = ((float) (currentDayUserCount - previousDayUserCount) / previousDayUserCount) * 100.0f;

        var roundGrowthPercentage = BigDecimal
                .valueOf(growthPercentage)
                .setScale(1, RoundingMode.HALF_UP)
                .floatValue();

        return GrowthPercentageResponse
                .builder()
                .currentData(currentDayUserCount)
                .previousData(previousDayUserCount)
                .growthPercentage(roundGrowthPercentage)
                .build();
    }

    @Override
    public List<Pair<String, Long>> calculateTotalOfUser() {
        List<Pair<String, Long>> list = new ArrayList<>();
        for (RoleType roleName : RoleType.values()) {
            long countUserByRoleName = userRepository
                    .findAll()
                    .stream()
                    .filter(user -> user.getRoles().getRoleName().equalsIgnoreCase(roleName.name()))
                    .count();

            list.add(Pair.of(roleName.name(), countUserByRoleName));
        }
        return list;
    }
}
