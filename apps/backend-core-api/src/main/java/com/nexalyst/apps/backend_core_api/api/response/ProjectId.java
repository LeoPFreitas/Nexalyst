package com.nexalyst.apps.backend_core_api.api.response;

public record ProjectId(Integer projectId) {
    public ProjectId {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
    }
}
