package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;

import java.util.UUID;

public record UpdateCompanyUseCaseOut(
        CompanyUpdated companyUpdated
) implements ApplicationResponse {
    public record CompanyUpdated(
            UUID id,
            String nit,
            String legalName,
            boolean payrollTaxExempt,
            boolean active
    ) {}
}
