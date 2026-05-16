package com.finflow.auth.domain.valueobject;

import com.finflow.auth.domain.exception.DomainException;

import java.util.Objects;

public record PasswordHash(String value) {

    public PasswordHash {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new DomainException("Password hash must not be blank");
        }

        if (value.length() < 60) {
            throw new DomainException("Password hash format is invalid");
        }
    }
}
