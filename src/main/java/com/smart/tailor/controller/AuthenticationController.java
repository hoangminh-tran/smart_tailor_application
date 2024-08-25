package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.smart.tailor.config.CustomExeption;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.LinkConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.event.RegistrationCompleteEvent;
import com.smart.tailor.event.listener.RegistrationCompleteEventListener;
import com.smart.tailor.service.AuthenticationService;
import com.smart.tailor.service.LogoutService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.service.VerificationTokenService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import com.smart.tailor.utils.response.UserResponse;
import com.smart.tailor.validate.ValidEmail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping(APIConstant.AuthenticationAPI.AUTHENTICATION)
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;
    private final LogoutService logoutService;
    private final HttpServletResponse response;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @GetMapping(APIConstant.AuthenticationAPI.VERIFY + "/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable("token") String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            var message = authenticationService.verifyUser(token);
            if (message.equalsIgnoreCase(MessageConstant.TOKEN_IS_VALID)) {
//                respon.put("status", 200);
//                respon.put("message", MessageConstant.ACCOUNT_VERIFIED_SUCCESSFULLY);
//                return ResponseEntity.ok(respon);
                var verificationTokenOptional = verificationTokenService.findByToken(token);
                var verificationToken = verificationTokenOptional.get();
                User user = verificationToken.getUser();
                response.sendRedirect("http://localhost:3000/auth/verify/" + user.getEmail());
                return ResponseEntity.ok(MessageConstant.ACCOUNT_VERIFIED_SUCCESSFULLY);

            } else {
//                respon.put("status", 400);
//                respon.put("message", message);
                response.sendRedirect("https://tttm-mavericks.github.io/InvalidToken");
                return ResponseEntity.ok("FAIL");
            }
        } catch (Exception ex) {
            if (ex instanceof CustomExeption) {

            }
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(ex.getMessage());
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.RESEND_VERIFICATION_TOKEN + "/{email}")
    public ResponseEntity<ObjectNode> resendVerificationToken(@ValidEmail @PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            var verificationToken = verificationTokenService.generateNewVerificationToken(email);
            var user = verificationToken.getUser();
            var typeOfVerification = verificationToken.getTypeOfVerification();
            String verificationURL = LinkConstant.LINK_VERIFICATION_ACCOUNT + "/" + verificationToken.getToken();

            switch (typeOfVerification) {
                case VERIFY_ACCOUNT -> {
                    registrationCompleteEventListener.sendVerificationEmail(user, verificationURL);
                }
                case FORGOT_PASSWORD -> {
                    registrationCompleteEventListener.sendPasswordResetEmail(user, verificationURL);
                }
                case CHANGE_PASSWORD -> {
                    registrationCompleteEventListener.sendChangePasswordMail(user, verificationURL);
                }
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.RESEND_MAIL_NEW_TOKEN_SUCCESSFULLY);

            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.CHECK_VERIFY_ACCOUNT + "/{email}")
    public ResponseEntity<ObjectNode> checkVerifyAccount(@ValidEmail @PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {

            boolean isVerified = authenticationService.checkVerifyAccount(email);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_IS_VERIFIED);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN CHECK VERIFY PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.CHECK_VERIFY_FORGOT_PASSWORD + "/{email}")
    public ResponseEntity<ObjectNode> checkVerifyForgotPassword(@ValidEmail @PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = authenticationService.checkVerifyPassword(email, TypeOfVerification.FORGOT_PASSWORD);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_IS_VERIFIED);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", ErrorConstant.ACCOUNT_NOT_VERIFIED.getStatusCode());
                respon.put("message", ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN CHECK VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.CHECK_VERIFY_CHANGE_PASSWORD + "/{email}")
    public ResponseEntity<ObjectNode> checkVerifyChangePassword(@ValidEmail @PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = authenticationService.checkVerifyPassword(email, TypeOfVerification.CHANGE_PASSWORD);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_IS_VERIFIED);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", ErrorConstant.ACCOUNT_NOT_VERIFIED.getStatusCode());
                respon.put("message", ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN CHECK VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.REGISTER)
    public ResponseEntity<ObjectNode> register(@Valid @RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        var authenResponse = authenticationService.register(userRequest);
        var registeredUser = userService.getUserByEmail(authenResponse.getUser().getEmail());
        if (registeredUser.getProvider().equals(Provider.LOCAL)) {
            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(registeredUser, TypeOfVerification.VERIFY_ACCOUNT));
            logger.info("Publish Event When Register By Local Successfully");
            respon.put("message", MessageConstant.SEND_MAIL_FOR_VERIFY_ACCOUNT_SUCCESSFULLY);
        } else {
            respon.put("message", MessageConstant.REGISTER_NEW_USER_SUCCESSFULLY);
        }
        respon.put("status", 200);
        respon.set("data", objectMapper.valueToTree(authenResponse));
        return ResponseEntity.ok(respon);
    }

    @GetMapping(APIConstant.AuthenticationAPI.FORGOT_PASSWORD + "/{email}")
    public ResponseEntity<ObjectNode> forgotPassword(@ValidEmail @PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            var user = authenticationService.forgotPassword(email);
            if (user != null) {
                var verificationToken = verificationTokenService.findByUserID(user.getUserID());
                String verificationURL = LinkConstant.LINK_VERIFICATION_ACCOUNT + "/" + verificationToken.getToken();
                registrationCompleteEventListener.sendPasswordResetEmail(user, verificationURL);
                respon.put("status", HttpStatus.OK.value());
                respon.put("message", MessageConstant.SEND_MAIL_FOR_UPDATE_PASSWORD_SUCCESSFULLY);
            } else {
                respon.put("status", ErrorConstant.BAD_REQUEST.getStatusCode());
                respon.put("message", ErrorConstant.BAD_REQUEST.getMessage());
            }
            return ResponseEntity.ok(respon);

        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN SEND MAIL FORGOT PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.CHANGE_PASSWORD + "/{email}")
    public ResponseEntity<ObjectNode> changePassword(@ValidEmail @PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            var user = authenticationService.changePassword(email);
            if (user != null) {
                var verificationToken = verificationTokenService.findByUserID(user.getUserID());
                String verificationURL = LinkConstant.LINK_VERIFICATION_ACCOUNT + "/" + verificationToken.getToken();
                registrationCompleteEventListener.sendChangePasswordMail(user, verificationURL);
                respon.put("status", HttpStatus.OK.value());
                respon.put("message", MessageConstant.SEND_MAIL_FOR_UPDATE_PASSWORD_SUCCESSFULLY);
            } else {
                respon.put("status", ErrorConstant.BAD_REQUEST.getStatusCode());
                respon.put("message", ErrorConstant.BAD_REQUEST.getMessage());
            }
            return ResponseEntity.ok(respon);

        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN SEND MAIL FORGOT PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.UPDATE_PASSWORD)
    public ResponseEntity<ObjectNode> updatePassword(@Valid @RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            if (userRequest == null) {
                respon.put("status", ErrorConstant.BAD_REQUEST.getStatusCode());
                respon.put("message", ErrorConstant.BAD_REQUEST.getMessage());
                return ResponseEntity.ok(respon);
            }

            if (userRequest.getEmail() == null) {
                respon.put("status", ErrorConstant.MISSING_ARGUMENT.getStatusCode());
                respon.put("message", ErrorConstant.MISSING_ARGUMENT.getMessage());
                return ResponseEntity.ok(respon);
            }

            String email = userRequest.getEmail();
            if (!Utilities.isValidEmail(email)) {
                respon.put("status", ErrorConstant.INVALID_EMAIL.getStatusCode());
                respon.put("message", ErrorConstant.INVALID_EMAIL.getMessage());
                return ResponseEntity.ok(respon);
            }

            if (userRequest.getPassword() == null) {
                respon.put("status", ErrorConstant.MISSING_ARGUMENT.getStatusCode());
                respon.put("message", ErrorConstant.MISSING_ARGUMENT.getMessage());
                return ResponseEntity.ok(respon);
            }
            String password = userRequest.getPassword();
            if (!Utilities.isValidPassword(password)) {
                respon.put("status", ErrorConstant.INVALID_PASSWORD.getStatusCode());
                respon.put("message", ErrorConstant.INVALID_PASSWORD.getMessage());
                return ResponseEntity.ok(respon);
            }

            UserResponse userResponse = authenticationService.updatePassword(userRequest);
            if (userResponse == null) {
                respon.put("status", ErrorConstant.UPDATE_PASSWORD_FAILED.getStatusCode());
                respon.put("message", ErrorConstant.UPDATE_PASSWORD_FAILED.getMessage());
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.UPDATE_PASSWORD_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(userResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN UPDATE PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.LOGIN)
    public ResponseEntity<ObjectNode> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
        respon.put("status", 200);
        respon.put("message", MessageConstant.LOGIN_SUCCESSFULLY);
        respon.set("data", objectMapper.valueToTree(authenticationResponse));
        return ResponseEntity.ok(respon);
    }

    @PostMapping(APIConstant.AuthenticationAPI.GOOGLE_LOGIN)
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            String token = payload.get("authRequest");
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance()).setAudience(Collections.singletonList(clientId)).build();
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload googlePayload = idToken.getPayload();
                String email = googlePayload.getEmail();
                String fullName = (String) googlePayload.get("name");
                String imageUrl = (String) googlePayload.get("picture");
                String language = (String) googlePayload.get("language");
                User user = userService.getUserByEmail(email);
                if (user == null) {

                    ResponseEntity<ObjectNode> response = register(UserRequest.builder().email(email).password(clientId).provider(Provider.GOOGLE).language(language).roleName("CUSTOMER").fullName(fullName).build());

                    if (response.getStatusCode().is2xxSuccessful()) {
                        UserResponse userResponse = objectMapper.treeToValue(response.getBody().get("data").get("user"), UserResponse.class);
                        userResponse.setImageUrl(imageUrl);

                        AuthenticationResponse authenticationResponse = objectMapper.treeToValue(response.getBody().get("data"), AuthenticationResponse.class);
                        authenticationResponse.setUser(userResponse);
                        respon.put("status", 200);
                        respon.put("message", MessageConstant.REGISTER_NEW_USER_SUCCESSFULLY);
                        respon.set("data", objectMapper.valueToTree(authenticationResponse));
                        return ResponseEntity.ok(respon);
                    }
                    return response;
                }
                return login(AuthenticationRequest.builder().provider(Provider.GOOGLE).email(email).password(clientId).build());
            }
            respon.put("status", ErrorConstant.INVALID_VERIFICATION_TOKEN.getStatusCode());
            respon.put("message", ErrorConstant.INVALID_VERIFICATION_TOKEN.getMessage());
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", ErrorConstant.INTERNAL_SERVER_ERROR.getStatusCode());
            respon.put("message", ErrorConstant.INTERNAL_SERVER_ERROR.getMessage());
            logger.error("ERROR IN GOOGLE LOGIN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.REFRESH_TOKEN)
    public ResponseEntity<ObjectNode> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request, response);
            if (authenticationResponse == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.REFRESH_TOKEN_FAILED);
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.REFRESH_TOKEN_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN REFRESH TOKEN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.LOG_OUT)
    public ResponseEntity<ObjectNode> logout(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logoutService.logout(request, response, authentication);
            respon.put("status", HttpStatus.OK.value());
            respon.put("message", "Logout successfully");
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            // logger.error("ERROR IN LOGOUT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }
}