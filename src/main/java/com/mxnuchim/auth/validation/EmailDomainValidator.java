package com.mxnuchim.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail, String> {

    private final Set<String> blockedDomains;

    public EmailDomainValidator(
            @Value("${app.security.disposable-emails}") final List<String> blockedDomains
    ) {
        this.blockedDomains = blockedDomains.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(final String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null || !email.contains("@")) {
            return true; // Let @Email annotation handle format validation
        }

        final String domain = email.substring(email.indexOf("@") + 1).toLowerCase();

        return !this.blockedDomains.contains(domain);
    }
}