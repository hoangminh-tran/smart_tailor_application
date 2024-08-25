package com.smart.tailor.service.impl;

import com.smart.tailor.entities.User;
import com.smart.tailor.entities.VerificationToken;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.repository.VerificationTokenRepository;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.service.VerificationTokenService;
import com.smart.tailor.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final SystemPropertiesService systemPropertiesService;

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken findByUserID(String userID) {
        return verificationTokenRepository.findByUserID(userID);
    }

    @Override
    public void saveUserVerificationToken(User user, String token, TypeOfVerification typeOfVerification) {
        LocalDateTime localDateTime = LocalDateTime.now();
        VerificationToken existedVerificationToken = findByUserID(user.getUserID());
        var emailVerificationTime = Integer.parseInt(systemPropertiesService.getByName("EMAIL_VERIFICATION_TIME").getPropertyValue());
        if (existedVerificationToken == null) {
            verificationTokenRepository.save(
                    VerificationToken
                            .builder()
                            .token(token)
                            .user(user)
                            .typeOfVerification(typeOfVerification)
                            .isEnabled(false)
                            .expirationDateTime(localDateTime.plusMinutes(emailVerificationTime))
                            .build()
            );
        } else {
            existedVerificationToken.setTypeOfVerification(typeOfVerification);
            existedVerificationToken.setToken(token);
            existedVerificationToken.setEnabled(false);
            existedVerificationToken.setExpirationDateTime(localDateTime.plusMinutes(emailVerificationTime));
            verificationTokenRepository.save(existedVerificationToken);
        }
    }

    @Override
    public VerificationToken generateNewVerificationToken(String userEmail) {
        var verificationToken = findVerificationTokenByUserEmail(userEmail);
        var emailVerificationTime = Integer.parseInt(systemPropertiesService.getByName("EMAIL_VERIFICATION_TIME").getPropertyValue());
        if (verificationToken != null) {
            verificationToken.setToken(Utilities.generateCustomPrimaryKey());
            verificationToken.setExpirationDateTime(LocalDateTime.now().plusMinutes(emailVerificationTime));
            verificationToken.setEnabled(false);
            return verificationTokenRepository.save(verificationToken);
        }
        return null;
    }

    @Override
    public VerificationToken findVerificationTokenByUserEmail(String userEmail) {
        if (!Utilities.isStringNotNullOrEmpty(userEmail)) {
            return null;
        }
        if (!Utilities.isValidEmail(userEmail)) {
            return null;
        }
        return verificationTokenRepository.findVerificationTokenByUserEmail(userEmail);
    }

    @Override
    public void enableVerificationToken(VerificationToken verificationToken) {
        verificationToken.setEnabled(true);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void deleteVerificationTokenByUserID(String userID) {
        verificationTokenRepository.deleteVerificationTokenByUserID(userID);
    }
}
