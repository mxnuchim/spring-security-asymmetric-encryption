package com.mxnuchim.auth.repositories;

import com.mxnuchim.auth.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCaseAndEnabledTrue(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByIdAndEnabledTrue(UUID id);
}

