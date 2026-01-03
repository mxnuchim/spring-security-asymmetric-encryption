package com.mxnuchim.auth.mappers;

import com.mxnuchim.auth.dto.ProfileUpdateDto;
import com.mxnuchim.auth.domain.entities.User;
import com.mxnuchim.auth.dto.request.RegistrationRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public void mergeUserInfo(final User user, final ProfileUpdateDto request) {
        if (StringUtils.isNotBlank(request.getFirstName()) && !user.getFirstName()
                .equals(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (StringUtils.isNotBlank(request.getLastName()) && !user.getLastName()
                .equals(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if (request.getDateOfBirth() != null && !request.getDateOfBirth()
                .equals(user.getDateOfBirth())) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
    }

    public User toUser(RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(request.getPassword())
                .enabled(true)
                .credentialsExpired(false)
                .accountLocked(false)
                .emailVerified(false)
                .phoneVerified(false)
                .build();
    };
}
