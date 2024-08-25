package com.smart.tailor.service;


import com.smart.tailor.entities.Token;

import java.util.List;
import java.util.Optional;


public interface TokenService {
    Optional<Token> findByToken(String token);

    void saveToken(Token token);

    boolean findTokenWithNotExpiredAndNotRevoked(String token);

    List<Token> findAllValidTokenByUser(String userID);

    void revokeAllUserTokens(List<Token> tokens);

    void deleteTokenByUserID(String userID);
}
