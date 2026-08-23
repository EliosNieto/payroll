package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;

import java.util.UUID;

public record CreateCompanyUseCaseOut(
        CompanyCreated companyCreated
) implements ApplicationResponse {
    public record CompanyCreated(UUID id, String nit, String legalName, boolean payrollTaxExempt, boolean active){}
}
