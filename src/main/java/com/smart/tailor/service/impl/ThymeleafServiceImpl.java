package com.smart.tailor.service.impl;

import com.smart.tailor.service.ThymeleafService;
import com.smart.tailor.utils.response.OrderCustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThymeleafServiceImpl implements ThymeleafService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "/mail/MailMessages";
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";
    private static final String TEMPLATE_VERIFY_ACCOUNT = "TemplateVerifyAccount";
    private static final String TEMPLATE_RESET_PASSWORD = "TemplateResetPassword";
    private static final String TEMPLATE_CHANGE_PASSWORD = "TemplateChangePassword";
    private static final String TEMPLATE_SELECTED_BRAND_FOR_SPECIFIC_ORDER = "TemplateSelectedBrandForSpecificOrder";
    private static final TemplateEngine templateEngine;

    static {
        templateEngine = emailTemplateEngine();
    }

    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }

    @Override
    public String createThymeleafForVerifyAccount(String email, String verificationUrl) {
        final Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("verificationUrl", verificationUrl);
        return templateEngine.process(TEMPLATE_VERIFY_ACCOUNT, context);
    }

    @Override
    public String createThymeleafForResetPassword(String email, String verificationUrl) {
        final Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("verificationUrl", verificationUrl);
        return templateEngine.process(TEMPLATE_RESET_PASSWORD, context);
    }

    @Override
    public String createThymeleafForChangePassword(String email, String verificationUrl) {
        final Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("verificationUrl", verificationUrl);
        return templateEngine.process(TEMPLATE_CHANGE_PASSWORD, context);
    }

    @Override
    public String createThymeleafForSelectedBrandForSpecificOrder(String email, OrderCustomResponse orderResponse, String sendMailSelectedBrandsForOrderLink) {
        final Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("orderID", orderResponse.getOrderID());
        context.setVariable("detailList", orderResponse.getDetailList());
        context.setVariable("sendMailSelectedBrandsForOrderLink", sendMailSelectedBrandsForOrderLink);
        return templateEngine.process(TEMPLATE_SELECTED_BRAND_FOR_SPECIFIC_ORDER, context);
    }
}
