package com.smart.tailor.repository;

import com.smart.tailor.entities.VerificationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);

    @Query(value = "select * from verification_token where user_id = ?1", nativeQuery = true)
    VerificationToken findByUserID(String userID);

    @Query(value = "select v.* from verification_token v join users u on v.user_id = u.user_id" +
            " where u.email = ?1", nativeQuery = true)
    VerificationToken findVerificationTokenByUserEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "delete from verification_token v where v.user_id = ?1", nativeQuery = true)
    void deleteVerificationTokenByUserID(String userID);
}
