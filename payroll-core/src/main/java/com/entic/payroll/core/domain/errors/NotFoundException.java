package com.entic.payroll.core.domain.errors;

import com.entic.payroll.core.application.commons.errors.ApplicationError;

import java.util.HashMap;
import java.util.Map;

public class NotFoundException extends ApplicationError {

    private final String field;
    private final String value;
    private final String errorKey;

    public NotFoundException(String field, String value, String errorKey) {
        this.field = field;
        this.value = value;
        this.errorKey = errorKey;
    }

    @Override
    public String getMessage() {
        return errorKey;
    }

    @Override
    public String errorCode() {
        return "notFound";
    }

    @Override
    public Map<String, Object> metadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(field, value);
        return metadata;
    }
}
