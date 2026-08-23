package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;

import java.util.UUID;

public record GetCompanyByIdUseCaseOut(
        CompanyFound companyFound
) implements ApplicationResponse {
    public record CompanyFound(
            UUID id,
            String nit,
            String legalName,
            boolean payrollTaxExempt,
            boolean active
    ) {}
}
