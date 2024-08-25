package com.smart.tailor.service;

import com.smart.tailor.entities.Customer;
import com.smart.tailor.entities.User;
import com.smart.tailor.utils.request.CustomerRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.CustomerResponse;

import java.util.Optional;


public interface CustomerService {
    void createCustomer(User user, Boolean gender);

    Optional<Customer> findById(String customerID);

    APIResponse updateCustomerProfile(String jwtToken, String customerID, CustomerRequest customerRequest);

    CustomerResponse getCustomerByUserID(String userID);

    CustomerResponse mapperToCustomerResponse(Customer customer);
}
