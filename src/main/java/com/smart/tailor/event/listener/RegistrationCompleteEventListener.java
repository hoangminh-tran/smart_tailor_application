package com.smart.tailor.event.listener;

import com.smart.tailor.constant.LinkConstant;
import com.smart.tailor.entities.User;
import com.smart.tailor.event.RegistrationCompleteEvent;
import com.smart.tailor.service.MailService;
import com.smart.tailor.service.VerificationTokenService;
import com.smart.tailor.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final VerificationTokenService verificationTokenService;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        var registeredUser = event.getUser();
        String verificationToken = Utilities.generateCustomPrimaryKey();
        var typeOfVerification = event.getTypeOfVerification();

        verificationTokenService.saveUserVerificationToken(registeredUser, verificationToken, typeOfVerification);
        String verificationURL = LinkConstant.LINK_VERIFICATION_ACCOUNT + "/" + verificationToken;
        sendVerificationEmail(registeredUser, verificationURL);

        logger.info("The link to verify account registration {}", LinkConstant.LINK_VERIFICATION_ACCOUNT);
    }

    public void sendVerificationEmail(User registeredUser, String verificationUrl) {
        mailService.sendMailVerifyAccount(registeredUser.getEmail(), "Email Verification", verificationUrl);
    }

    public void sendPasswordResetEmail(User registeredUser, String verificationUrl) {
        mailService.sendMailResetPassword(registeredUser.getEmail(), "Reset Password Request", verificationUrl);
    }

    public void sendChangePasswordMail(User registeredUser, String verificationUrl) {
        mailService.sendMailChangePassword(registeredUser.getEmail(), "Change Password Request", verificationUrl);
    }
}
