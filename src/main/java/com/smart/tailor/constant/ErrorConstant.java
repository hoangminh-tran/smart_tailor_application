package com.smart.tailor.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorConstant {
    INTERNAL_SERVER_ERROR(-1, "An internal server error occurred."),
    MISSING_ARGUMENT(400, "Missing Argument!"),
    BAD_REQUEST(400, "Bad Request!"),
    INVALID_EMAIL(400, "Email is Invalid!"),
    INVALID_ID(400, "ID is Invalid!"),
    INVALID_PASSWORD(400, "Password is Invalid!"),
    ACCOUNT_NOT_VERIFIED(400, "Account Is Not Verified!"),
    REGISTER_NEW_USER_FAILED(400, "Failed To Register New User!"),
    UPDATE_PASSWORD_FAILED(400, "Failed To Update Password!"),
    INVALID_EMAIL_OR_PASSWORD(400, "Invalid Email Or Password!"),
    INVALID_VERIFICATION_TOKEN(401, "Invalid Verification Token!"),
    UNAUTHORIZED(401, "Unauthorized!"),
    PAYMENT_REQUIRED(402, "Payment Required!"),
    DUPLICATE_REGISTERED_EMAIL(409, "Duplicate Registered Email!"),
    ;

    private final Integer statusCode;
    private final String message;
}
