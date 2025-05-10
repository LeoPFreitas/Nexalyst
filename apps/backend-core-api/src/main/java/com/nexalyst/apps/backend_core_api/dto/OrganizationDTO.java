package com.nexalyst.apps.backend_core_api.dto;

import java.util.UUID;

public record OrganizationDTO(
        UUID id,
        String name,
        String description
) {
}
