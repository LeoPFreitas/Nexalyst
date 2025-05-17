package com.nexalyst.apps.backend_core_api.api.request;

import jakarta.validation.constraints.NotNull;

public record UpdateProjectRequest(
        @NotNull Integer projectId,
        @NotNull String name,
        String description
) {
}
