package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;

import java.util.UUID;

public record CreateEmployeeUseCaseOut(
        EmployeeCreated employeeCreated
) implements ApplicationResponse {

    public record EmployeeCreated(
            Long id,
            UUID companyId,
            DocumentType documentType,
            String documentNumber,
            String firstNames,
            String lastNames,
            WorkerType workerType
    ) {}
}