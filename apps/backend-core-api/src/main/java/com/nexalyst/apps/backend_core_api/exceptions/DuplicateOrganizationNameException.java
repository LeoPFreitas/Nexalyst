package com.nexalyst.apps.backend_core_api.exceptions;

public class DuplicateOrganizationNameException extends RuntimeException {

    public DuplicateOrganizationNameException(String message) {
        super(message);
    }
}