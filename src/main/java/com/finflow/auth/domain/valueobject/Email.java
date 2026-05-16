package com.finflow.auth.domain.valueobject;

import com.finflow.auth.domain.exception.DomainException;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email (String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    public Email {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new DomainException("Email must not be blank");
        }

        String normalizedEmail = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new DomainException("Email format is invalid");
        }
        value = normalizedEmail;
    }
}