package com.smart.tailor.service;

import com.smart.tailor.utils.response.OrderCustomResponse;

public interface MailService {
    void sendMailVerifyAccount(String emailTo, String emailSubject, String verificationUrl);

    void sendMailResetPassword(String emailTo, String emailSubject, String verificationUrl);

    void sendMailChangePassword(String emailTo, String emailSubject, String verificationUrl);

    void sendMailToSelectedBrandsForSpecificOrder(String emailTo, String emailSubject, String sendMailSelectedBrandsForOrderLink, OrderCustomResponse orderResponse);
}
