package com.mxnuchim.auth.repositories;

import com.mxnuchim.auth.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String roleUser);
}
