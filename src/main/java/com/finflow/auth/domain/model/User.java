package com.finflow.auth.domain.model;

import com.finflow.auth.domain.exception.DomainException;
import com.finflow.auth.domain.valueobject.Email;
import com.finflow.auth.domain.valueobject.PasswordHash;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private final Email email;
    private final PasswordHash passwordHash;
    private final String fullName;
    private UserStatus status;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private User(
            UUID id,
            Email email,
            PasswordHash passwordHash,
            String fullName,
            UserStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "User id must not be null");
        this.email = Objects.requireNonNull(email, "User email must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash must not be null");
        this.fullName = validatefullName(fullName);
        this.status = Objects.requireNonNull(status, "User status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at must not be null");
    }

    public static User register(
            Email email,
            PasswordHash passwordHash,
            String fullName
    ) {
        OffsetDateTime now =  OffsetDateTime.now();

        return new User(
                UUID.randomUUID(),
                email,
                passwordHash,
                fullName,
                UserStatus.PENDING_VERIFICATION,
                now,
                now
        );
    }
    public void activate() {
        if (status == UserStatus.DISABLED) {
            throw new DomainException("Disabled user cannot be activated");
        }
        this.status = UserStatus.ACTIVE;
        this.updatedAt = OffsetDateTime.now();
    }

    public void lock() {
        if (status == UserStatus.DISABLED) {
            throw new DomainException("Disabled user cannot be locked");
        }

        this.status = UserStatus.LOCKED;
        this.updatedAt = OffsetDateTime.now();
    }

    public void disable() {
        this.status = UserStatus.DISABLED;
        this.updatedAt = OffsetDateTime.now();
    }

    public boolean canAuthenticate() {
        return status == UserStatus.ACTIVE;
    }

    private static String validatefullName(String fullName) {
        if (Objects.isNull(fullName) || fullName.isBlank()) {
            throw new DomainException("Full name must be not blank");
        }

        String normalizedFullName =  fullName.trim();

        if (normalizedFullName.length() < 2 || normalizedFullName.length() > 255) {
            throw new DomainException("Full name must be 2 and 255 characters");
        }

        return normalizedFullName;
    }

    public UUID getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
