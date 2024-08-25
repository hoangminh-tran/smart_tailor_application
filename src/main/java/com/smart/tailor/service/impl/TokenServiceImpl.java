package com.smart.tailor.service.impl;


import com.smart.tailor.entities.Token;
import com.smart.tailor.repository.TokenRepository;
import com.smart.tailor.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public boolean findTokenWithNotExpiredAndNotRevoked(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    @Override
    public List<Token> findAllValidTokenByUser(String userID) {
        return tokenRepository.findAllValidTokenByUser(userID);
    }

    @Override
    public void revokeAllUserTokens(List<Token> tokens) {
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void deleteTokenByUserID(String userID) {
        tokenRepository.deleteTokenByUserID(userID);
    }
}