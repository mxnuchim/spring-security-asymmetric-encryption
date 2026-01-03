package com.mxnuchim.auth.config;

import com.mxnuchim.auth.domain.entities.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class ApplicationAuditorWare implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(
                authentication == null ||
                        !authentication.isAuthenticated() ||
                        authentication instanceof AnonymousAuthenticationToken
        ){
            return Optional.empty();
        }

        final User user = (User) authentication.getPrincipal();
        return Optional.ofNullable(user.getId());
    }
}
