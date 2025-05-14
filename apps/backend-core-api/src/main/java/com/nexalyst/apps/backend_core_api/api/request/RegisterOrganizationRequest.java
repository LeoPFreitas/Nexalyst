package com.nexalyst.apps.backend_core_api.api.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterOrganizationRequest(
        @NotBlank(message = "Organization name is required")
        String name,
        String description
) {
}
