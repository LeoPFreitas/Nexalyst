package com.nexalyst.apps.backend_core_api.api.request;

import java.util.UUID;


public record UpdateOrganizationRequest(
        UUID organizationId,
        String name,
        String description
) {
}
