package com.smart.tailor.service;

import com.smart.tailor.utils.response.OrderCustomResponse;

public interface ThymeleafService {
    String createThymeleafForVerifyAccount(String email, String verificationUrl);

    String createThymeleafForResetPassword(String email, String verificationUrl);

    String createThymeleafForChangePassword(String email, String verificationUrl);

    String createThymeleafForSelectedBrandForSpecificOrder(String email, OrderCustomResponse orderResponse, String sendMailSelectedBrandsForOrderLink);
}
