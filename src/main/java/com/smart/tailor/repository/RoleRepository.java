package com.smart.tailor.repository;


import com.smart.tailor.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {
    Optional<Roles> findByRoleName(String role_name);
}
