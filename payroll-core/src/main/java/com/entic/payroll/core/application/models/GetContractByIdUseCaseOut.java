package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;
import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.core.domain.enums.RiskLevel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record GetContractByIdUseCaseOut(
        ContractFound contractFound
) implements ApplicationResponse {

    public record ContractFound(
            Long id,
            UUID companyId,
            Long employeeId,
            ContractType contractType,
            BigDecimal baseSalary,
            boolean integralSalary,
            RiskLevel riskLevelArl,
            LocalDate startDate,
            LocalDate endDate
    ) {}
}
