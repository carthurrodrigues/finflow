package com.finflow.auth.domain.model;

import com.finflow.auth.domain.exception.DomainException;
import com.finflow.auth.domain.valueobject.PasswordHash;
import com.finflow.auth.domain.valueobject.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserTest {
    private static final String VALID_PASSWORD_HASH =
            "$2a$12$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234";

    @Test
    void shouldRegisterUserWithPendingVerificationStatus() {
        User user = User.register(
                new Email("arthur@finflow.com"),
                new PasswordHash(VALID_PASSWORD_HASH),
                "Arthur Rodrigues"
        );

        assertNotNull(user.getId());
        assertEquals("arthur@finflow.com", user.getEmail().value());
        assertEquals("Arthur Rodrigues", user.getFullName());
        assertEquals(UserStatus.PENDING_VERIFICATION, user.getStatus());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertFalse(user.canAuthenticate());
    }

    @Test
    void shouldActivateUser() {
        User user = User.register(
                new Email("arthur@finflow.com"),
                new PasswordHash(VALID_PASSWORD_HASH),
                "Arthur Rodrigues"
        );
        user.activate();

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertTrue(user.canAuthenticate());
    }

    @Test
    void shouldLockUser() {
        User user = User.register(
                new Email("arthur@finflow.com"),
                new PasswordHash(VALID_PASSWORD_HASH),
                "Arthur Rodrigues"
        );

        user.lock();
        assertEquals(UserStatus.LOCKED, user.getStatus());
        assertFalse(user.canAuthenticate());

    }

    @Test
    void shouldDisableUser() {
        User user = User.register(
                new Email("arthur@finflow.com"),
                new PasswordHash(VALID_PASSWORD_HASH),
                "Arthur Rodrigues"
        );

        user.disable();

        assertEquals(UserStatus.DISABLED, user.getStatus());
        assertFalse(user.canAuthenticate());
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsBlank() {
            assertThrows(
                    DomainException.class,
                    () -> User.register(
                            new Email ("arthur@finflow.com"),
                            new PasswordHash(VALID_PASSWORD_HASH),
                            " "
                    )
            );
}

    @Test
    void shouldNotLockDisabledUser() {
        User user = User.register(
                new Email("arthur@finflow.com"),
                new PasswordHash(VALID_PASSWORD_HASH),
                "Arthur Rodrigues"
        );

        user.disable();

        assertThrows(DomainException.class, user::lock);
    }
}
