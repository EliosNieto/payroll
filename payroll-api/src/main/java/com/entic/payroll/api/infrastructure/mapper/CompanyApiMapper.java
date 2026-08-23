package com.entic.payroll.api.infrastructure.mapper;

import com.entic.payroll.api.infrastructure.dto.company.CompanyResponse;
import com.entic.payroll.api.infrastructure.dto.company.CreateCompanyRequest;
import com.entic.payroll.api.infrastructure.dto.company.UpdateCompanyRequest;
import com.entic.payroll.core.application.models.CreateCompanyUseCaseIn;
import com.entic.payroll.core.application.models.UpdateCompanyUseCaseIn;
import com.entic.payroll.core.domain.company.Company;

import java.util.UUID;

public final class CompanyApiMapper {

    private CompanyApiMapper() {}

    public static CreateCompanyUseCaseIn toCommand(CreateCompanyRequest request) {
        return new CreateCompanyUseCaseIn(
                request.nit(),
                request.legalName(),
                request.payrollTaxExempt()
        );
    }

    public static UpdateCompanyUseCaseIn toCommand(UUID id, UpdateCompanyRequest request) {
        return new UpdateCompanyUseCaseIn(
                id,
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
