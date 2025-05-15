package com.nexalyst.apps.backend_core_api.exceptions;

public class DuplicatedProjectNameException extends RuntimeException {

    public DuplicatedProjectNameException(String message) {
        super(message);
    }
}