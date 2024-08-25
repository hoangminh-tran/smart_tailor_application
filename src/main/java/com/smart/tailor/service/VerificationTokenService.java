package com.smart.tailor.service;

import com.smart.tailor.entities.User;
import com.smart.tailor.entities.VerificationToken;
import com.smart.tailor.enums.TypeOfVerification;

import java.util.Optional;


public interface VerificationTokenService {
    Optional<VerificationToken> findByToken(String token);

    VerificationToken findByUserID(String userID);

    void saveUserVerificationToken(User user, String token, TypeOfVerification typeOfVerification);

    VerificationToken generateNewVerificationToken(String userEmail);

    VerificationToken findVerificationTokenByUserEmail(String userEmail);

    void enableVerificationToken(VerificationToken verificationToken);

    void deleteVerificationTokenByUserID(String userID);
}
