package com.nexalyst.apps.backend_core_api.api.request;

import com.nexalyst.apps.backend_core_api.api.response.OrganizationId;
import jakarta.validation.constraints.NotNull;

public record CreateProjectRequest(
        @NotNull OrganizationId organizationId,
        @NotNull String name,
        String description
) {
}
