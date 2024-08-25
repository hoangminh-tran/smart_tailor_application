package com.smart.tailor.repository;


import com.smart.tailor.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query(value = "select u.* from users u where u.email = ?1", nativeQuery = true)
    User getByEmail(String email);

    @Query(value = "select u.* from users u where u.phone_number = ?1", nativeQuery = true)
    User getByPhoneNumber(String phoneNumber);

    @Query(value = "select u.* from users u join verification_token vt on u.user_id = vt.user_id " +
            "where vt.is_enabled = ?1 && vt.type_of_verification = ?2", nativeQuery = true)
    List<User> getAllUserWithEmailUnverified(boolean isEnabled, String typeOfVerification);

    @Transactional
    @Modifying
    @Query(value = "delete from users u where u.user_id = ?1", nativeQuery = true)
    void deleteUserByUserID(String userID);
}
