package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;

public record UpdateEmployeeUseCaseIn(
        Long id,
        DocumentType documentType,
        String documentNumber,
        String firstNames,
        String lastNames,
        WorkerType workerType
) implements ApplicationRequest {
}