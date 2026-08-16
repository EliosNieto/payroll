package com.entic.payroll.api.infrastructure.dto.errors;

import java.util.Map;

public record ErrorFieldResponse(
        Map<String, Object> details,
        String errorKey,
        String message
) {}
