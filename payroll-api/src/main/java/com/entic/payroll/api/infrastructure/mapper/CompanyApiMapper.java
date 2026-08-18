package com.entic.payroll.api.infrastructure.mapper;

import com.entic.payroll.api.infrastructure.dto.company.CompanyResponse;
import com.entic.payroll.api.infrastructure.dto.company.CreateCompanyRequest;
import com.entic.payroll.core.application.company.CreateCompanyCommand;
import com.entic.payroll.core.domain.company.Company;

public final class CompanyApiMapper {

    private CompanyApiMapper() {}

    public static CreateCompanyCommand toCommand(CreateCompanyRequest request) {
        return new CreateCompanyCommand(
                request.nit(),
                request.legalName(),
                request.payrollTaxExempt()
        );
    }

    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getNit(),
                company.getLegalName(),
                company.isPayrollTaxExempt(),
                company.isActive()
        );
    }
}
