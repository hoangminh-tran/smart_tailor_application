package com.smart.tailor.service.impl;

import com.smart.tailor.constant.FormatConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Customer;
import com.smart.tailor.entities.User;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.mapper.CustomerMapper;
import com.smart.tailor.repository.CustomerRepository;
import com.smart.tailor.service.CustomerService;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.CustomerRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CustomerResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public void createCustomer(User user, Boolean gender) {
        customerRepository.save(
                Customer
                        .builder()
                        .gender(gender)
                        .user(user)
                        .build()
        );
    }

    @Override
    public Optional<Customer> findById(String customerID) {
        return customerRepository.findById(customerID);
    }

    @Override
    @Transactional
    public APIResponse updateCustomerProfile(String jwtToken, String customerID, CustomerRequest customerRequest) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!customerID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        if (!Utilities.isValidBoolean(customerRequest.getGender())) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " gender : " + customerRequest.getGender())
                    .data(null)
                    .build();

        }

        if (Utilities.isStringNotNullOrEmpty(customerRequest.getPhoneNumber())) {
            if (!Utilities.isValidVietnamesePhoneNumber(customerRequest.getPhoneNumber())) {
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.INVALID_DATA_TYPE + " phoneNumber : " + customerRequest.getPhoneNumber())
                        .data(null)
                        .build();
            }
        }


        if (userService.getUserByEmail(customerRequest.getEmail()) == null) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.EMAIL_IS_NOT_EXISTED)
                    .data(null)
                    .build();
        }

        if (!userService.getUserByEmail(customerRequest.getEmail()).getPhoneNumber().equals(customerRequest.getPhoneNumber())) {
            if (userService.getUserByPhoneNumber(customerRequest.getPhoneNumber()) != null) {
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.PHONE_NUMBER_IS_EXISTED)
                        .data(null)
                        .build();
            }
        }

        if (!Utilities.isValidDate(customerRequest.getDateOfBirth(), FormatConstant.DD_MM_YYYY_MINUS)) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " dateOfBirth : " + FormatConstant.DD_MM_YYYY_MINUS)
                    .data(null)
                    .build();
        }

        UserRequest userRequest = UserRequest
                .builder()
                .email(customerRequest.getEmail())
                .fullName(customerRequest.getFullName())
                .imageUrl(customerRequest.getImageUrl())
                .phoneNumber(customerRequest.getPhoneNumber())
                .build();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FormatConstant.DD_MM_YYYY_MINUS);
        Date formatDate = null;
        try {
            formatDate = simpleDateFormat.parse(customerRequest.getDateOfBirth());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        var userUpdated = userService.updateUserProfile(userRequest);

        Optional<Customer> customerOptional = customerRepository.findById(userUpdated.getUserID());
        if (customerOptional.isEmpty()) {
            logger.info("Inside Create Customer Profile Method");
            customerRepository.createCustomer(
                    userUpdated.getUserID(),
                    customerRequest.getGender(),
                    formatDate,
                    customerRequest.getAddress(),
                    customerRequest.getProvince(),
                    customerRequest.getDistrict(),
                    customerRequest.getWard()
            );
        } else {
            logger.info("Inside Update Customer Profile Method");
            customerRepository.updateCustomer(
                    customerRequest.getGender(),
                    formatDate,
                    customerRequest.getAddress(),
                    customerRequest.getProvince(),
                    customerRequest.getDistrict(),
                    customerRequest.getWard(),
                    userUpdated.getUserID()
            );
        }

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_PROFILE_CUSTOMER_SUCCESSFULLY)
                .data(null)
                .build();
    }

    @Override
    public CustomerResponse getCustomerByUserID(String userID) {
        Customer customer = customerRepository.findByCustomerID(userID);
        return mapperToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse mapperToCustomerResponse(Customer customer) {
        return customerMapper.mapperToCustomerResponse(customer);
    }
}
