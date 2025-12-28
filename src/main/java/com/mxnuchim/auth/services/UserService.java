package com.mxnuchim.auth.services;

import com.mxnuchim.auth.domain.dto.ChangePasswordDto;
import com.mxnuchim.auth.domain.dto.ProfileUpdateDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    void updateProfileInfo(UUID userId, ProfileUpdateDto dto);

    void changePassword(UUID userId, ChangePasswordDto dto);

    void deactivateAccount(UUID userId);

    void reactivateAccount(UUID userId);

    void deleteAccount(UUID userId);
}
