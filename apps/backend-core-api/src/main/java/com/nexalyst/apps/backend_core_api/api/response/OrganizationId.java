package com.nexalyst.apps.backend_core_api.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.NonNull;

import java.util.UUID;

public record OrganizationId(UUID organizationId) {
    public OrganizationId {
        if (organizationId == null) {
            throw new IllegalArgumentException("Organization ID cannot be null");
        }
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OrganizationId fromString(String organizationId) {
        return new OrganizationId(UUID.fromString(organizationId));
    }

    @JsonValue
    @NonNull
    public String toString() {
        return organizationId.toString();
    }
}