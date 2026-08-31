package com.entic.payroll.api.infrastructure.dto.employee;

import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;

import java.util.UUID;

public record CreateEmployeeRequest(
        UUID companyId,
        DocumentType documentType,
        String documentNumber,
        String firstNames,
        String lastNames,
        WorkerType workerType
) {}