package com.smart.tailor.config;

import com.smart.tailor.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/ws/**"
    };
    private static final String[] authenticatedRole = {
            RoleType.ADMIN.name(),
            RoleType.MANAGER.name(),
            RoleType.ACCOUNTANT.name(),
            RoleType.EMPLOYEE.name(),
            RoleType.BRAND.name(),
            RoleType.CUSTOMER.name(),
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final RestAccessDenyEntryPoint restAccessDenyEntryPoint;
    private final RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(access -> {
                    access.accessDeniedHandler(restAccessDenyEntryPoint);
                    access.authenticationEntryPoint(restUnauthorizedEntryPoint);
                })
                .authorizeHttpRequests(auth -> {
                            /* Authentication API */
                            auth.requestMatchers("/api/v1/auth/refresh-token").hasAnyRole(authenticatedRole);
                            auth.requestMatchers("/api/v1/auth/**").permitAll();

                            /* User API */
                            auth.requestMatchers("/api/v1/user/get-all-admin").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/user/calculate-user-growth-percentage-for-current-and-previous-week").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/user/calculate-new-customer-growth-percentage-for-current-and-previous-week").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/user/calculate-new-user-growth-percentage-for-current-and-previous-day-by-role/**").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/user/calculate-total-of-user").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/user/update-user/**").hasAnyRole(authenticatedRole);
                            auth.requestMatchers("/api/v1/user/**").hasAnyRole(RoleType.ADMIN.name(), RoleType.MANAGER.name());

                            /* Customer API */
                            auth.requestMatchers("/api/v1/customer/update-customer-profile/**").hasRole(RoleType.CUSTOMER.name());

                            /* Category API */
                            auth.requestMatchers("/api/v1/category/get-all-category").permitAll();
                            auth.requestMatchers("/api/v1/category/get-category-by-id").permitAll();
                            auth.requestMatchers("/api/v1/category/**").hasRole(RoleType.ADMIN.name());

                            /* Size API */
                            auth.requestMatchers("/api/v1/size/get-all-size").permitAll();
                            auth.requestMatchers("/api/v1/size/**").hasRole(RoleType.ADMIN.name());

                            /* Material API */
                            auth.requestMatchers("/api/v1/material/add-new-category-material-by-excel-file").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/material/update-material/**").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/material/add-new-material").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/material/update-status-material/**").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/material/generate-sample-category-material-by-excel-file").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/material/**").permitAll();

                            /* Brand Material API */
                            auth.requestMatchers("/api/v1/material/export-category-material-for-brand-by-excel").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand-material/**").hasRole(RoleType.BRAND.name());

                            /* Labor Quantity API */
                            auth.requestMatchers("/api/v1/labor-quantity/get-all-labor-quantity").hasAnyRole(RoleType.ADMIN.name(), RoleType.BRAND.name(), RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/labor-quantity/**").hasRole(RoleType.ADMIN.name());

                            /* Brand Labor Quantity API */
                            auth.requestMatchers("/api/v1/brand-labor-quantity/**").hasRole(RoleType.BRAND.name());

                            /* Brand Property API */
                            auth.requestMatchers("/api/v1/brand-property/get-brand-productivity-by-brand-id/**").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand-property/add-new-brand-property").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand-property/update-brand-property").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand-property/**").hasAnyRole(RoleType.ADMIN.name(), RoleType.MANAGER.name(), RoleType.BRAND.name());

                             /* Report API */
                            auth.requestMatchers("/api/v1/report/**").hasAnyRole(RoleType.EMPLOYEE.name(), RoleType.BRAND.name(), RoleType.CUSTOMER.name());
                            auth.requestMatchers("/api/v1/report/get-all-report-by-brand-id/**").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/report/get-all-report-by-user-id/**").hasRole(RoleType.CUSTOMER.name());

                            /* System Property API */
                            auth.requestMatchers("/api/v1/system-property/add-new-system-property").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/system-property/**").permitAll();

                            /* System Image API */
                            auth.requestMatchers("/api/v1/system-image/add-new-system-image").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/system-image/**").permitAll();

                            /* Expert Tailoring API */
                            auth.requestMatchers("/api/v1/expert-tailoring/add-new-expert-tailoring").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring/add-new-expert-tailoring-by-excel-file").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring/generate-sample-expert-tailoring-by-excel-file").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring/update-expert-tailoring/**").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring/update-status-expert-tailoring/**").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring/**").permitAll();

                            /* Expert Tailoring Material API */
                            auth.requestMatchers("/api/v1/expert-tailoring-material/add-new-expert-tailoring-material").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring-material/add-new-expert-tailoring-material-by-excel-file").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring-material/change-status-expert-tailoring-material/**").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring-material/generate-sample-category-material-expert-tailoring-by-excel-file").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/expert-tailoring-material/**").permitAll();

                            /* Size Expert Tailoring API */
                            auth.requestMatchers("/api/v1/size-expert-tailoring/add-new-size-expert-tailoring").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/size-expert-tailoring/update-size-expert-tailoring").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/size-expert-tailoring/add-new-size-expert-tailoring-by-excel-file").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/size-expert-tailoring/generate-sample-size-expert-tailoring-by-excel-file").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/size-expert-tailoring/**").permitAll();

                            /* Design API */
                            auth.requestMatchers("/api/v1/design/get-all-design-by-customer-id/**").hasRole(RoleType.CUSTOMER.name());
                            auth.requestMatchers("/api/v1/design/get-all-design-by-brand-id/**").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/design/get-all-design/**").permitAll();
                            auth.requestMatchers("/api/v1/design/get-design-by-id/**").permitAll();
                            auth.requestMatchers("/api/v1/design/**").hasAnyRole(authenticatedRole);

                            /* Part Of Design API */
                            auth.requestMatchers("/api/v1/part-of-design/**").permitAll();

                            /* Item Mask API */
                            auth.requestMatchers("/api/v1/item-mask/**").permitAll();

                            /* Brand API */
                            auth.requestMatchers("/api/v1/brand/accept-brand").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/brand/reject-brand").hasRole(RoleType.MANAGER.name());
                            auth.requestMatchers("/api/v1/brand/get-brand-registration-payment").hasAnyRole(RoleType.BRAND.name(), RoleType.MANAGER.name(), RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/brand/add-new-brand").permitAll();
                            auth.requestMatchers("/api/v1/brand/upload-brand-infor").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand/add-expert-tailoring-for-brand").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand/get-all-expert-tailoring-by-brand-id/**").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/brand/get-brand").hasAnyRole(RoleType.ADMIN.name(), RoleType.MANAGER.name(), RoleType.EMPLOYEE.name(), RoleType.BRAND.name(), RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/brand/get-all-brand-information").hasRole(RoleType.MANAGER.name());

                            /* Notification API */
                            auth.requestMatchers("/api/v1/notification/**").hasAnyRole(authenticatedRole);

                            /* Design Detail */
                            auth.requestMatchers("/api/v1/design-detail/**").hasAnyRole(authenticatedRole);

                            /* Order API */
                            auth.requestMatchers("/api/v1/order/get-order-status-detail").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/order/get-order-detail-of-each-brand").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/order/calculate-order-growth-percentage-for-current-and-previous-month").hasRole(RoleType.ADMIN.name());
                            auth.requestMatchers("/api/v1/order/get-order-by-brand-id/**").hasRole(RoleType.BRAND.name());
                            auth.requestMatchers("/api/v1/order/get-sub-order-invoice-by-sub-order-id/**").permitAll();
                            auth.requestMatchers("/api/v1/order/**").hasAnyRole(authenticatedRole);

                            /* Payment API */
                            auth.requestMatchers("/api/v1/payment/calculate-payment-growth-percentage-for-current-and-previous-week").hasRole(RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/payment/calculate-income-growth-percentage-for-current-and-previous-week").hasRole(RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/payment/calculate-refund-growth-percentage-for-current-and-previous-month").hasRole(RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/payment/get-total-payment-of-each-month").hasRole(RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/payment/get-total-refund-of-each-month").hasRole(RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/payment/get-total-income-of-each-month").hasRole(RoleType.ACCOUNTANT.name());
                            auth.requestMatchers("/api/v1/payment/**").hasAnyRole(authenticatedRole);

                            auth.requestMatchers(WHITE_LIST_URL).permitAll();
                            auth.anyRequest().permitAll();
                        }
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCORSFilter(), WebAsyncManagerIntegrationFilter.class);
        return httpSecurity.build();
    }
}
