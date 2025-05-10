package com.nexalyst.apps.backend_core_api.dto;

import jakarta.validation.constraints.NotBlank;

public record OrganizationRegistrationRequest(
        @NotBlank(message = "Organization name is required")
        String name,
        String description
) {
}
