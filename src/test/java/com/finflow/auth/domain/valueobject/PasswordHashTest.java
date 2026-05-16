package com.finflow.auth.domain.valueobject;

import com.finflow.auth.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordHashTest {

    @Test
    void shouldCreatePasswordHashWhenValueIsValid() {
        String hash = "$2a$12$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234";

        PasswordHash passwordHash = new PasswordHash(hash);

        assertEquals(hash, passwordHash.value());
    }

    @Test
    void shouldThrowExceptionWhenPasswordHashIsBlank() {
        assertThrows(DomainException.class, () -> new PasswordHash(" "));
    }

    @Test
    void shouldThrowExceptionWhenPasswordHashIsTooShort() {
        assertThrows(DomainException.class, () -> new PasswordHash("short"));
    }
}