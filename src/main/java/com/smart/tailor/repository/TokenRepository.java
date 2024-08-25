package com.smart.tailor.repository;


import com.smart.tailor.entities.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    @Query(value = "select t.* from token t join users u on t.user_id = u.user_id " +
            "where u.user_id = ?1 and (t.expired = 'false' or t.revoked = 'false')", nativeQuery = true)
    List<Token> findAllValidTokenByUser(String userID);

    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query(value = "delete from token t where t.user_id = ?1", nativeQuery = true)
    void deleteTokenByUserID(String userID);
}