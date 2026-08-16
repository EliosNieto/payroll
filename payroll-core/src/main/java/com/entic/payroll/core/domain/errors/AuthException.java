package com.entic.payroll.core.domain.errors;

import com.entic.payroll.core.application.commons.errors.ApplicationError;

public class AuthException extends ApplicationError {

    private final String message;

    public AuthException(String message) {
        this.message = message;
    }

    @Override
    public String errorCode() {
        return "authError";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
