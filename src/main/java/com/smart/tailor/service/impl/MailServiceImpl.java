package com.smart.tailor.service.impl;

import com.smart.tailor.service.MailService;
import com.smart.tailor.service.ThymeleafService;
import com.smart.tailor.utils.response.OrderCustomResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final ThymeleafService thymeleafService;
    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendMailVerifyAccount(String emailTo, String emailSubject, String verificationUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setSubject(emailSubject);
            helper.setText(thymeleafService.createThymeleafForVerifyAccount(emailTo, verificationUrl), true);
            logger.info("Inside Send Mail Verify Account Method");
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMailChangePassword(String emailTo, String emailSubject, String verificationUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setSubject(emailSubject);
            helper.setText(thymeleafService.createThymeleafForChangePassword(emailTo, verificationUrl), true);
            logger.info("Inside Send Mail Change Password Method");
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMailResetPassword(String emailTo, String emailSubject, String verificationUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setSubject(emailSubject);
            helper.setText(thymeleafService.createThymeleafForResetPassword(emailTo, verificationUrl), true);
            logger.info("Inside Send Mail Reset Password Method");
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMailToSelectedBrandsForSpecificOrder(String emailTo, String emailSubject, String sendMailSelectedBrandsForOrderLink, OrderCustomResponse orderResponse) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(emailFrom);
            helper.setTo(emailTo);
            helper.setSubject(emailSubject);
            helper.setText(thymeleafService.createThymeleafForSelectedBrandForSpecificOrder(emailTo, orderResponse, sendMailSelectedBrandsForOrderLink), true);
            logger.info("Inside Send Mail to Selected Brands For Specific Order Method");
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

