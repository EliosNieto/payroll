package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.GetCompanyByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetCompanyByIdUseCaseOut;
import com.entic.payroll.core.application.port.in.GetCompanyByIdUseCase;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCompanyByIdUseCaseImpl implements GetCompanyByIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetCompanyByIdUseCaseImpl.class);

    private final CompanyRepositoryPort repositoryPort;

    public GetCompanyByIdUseCaseImpl(CompanyRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GetCompanyByIdUseCaseOut execute(GetCompanyByIdUseCaseIn request) {
        log.info("Getting company by id: {}", request.id());

        validateId(request.id());

        Company company = repositoryPort.findById(request.id());

        log.info("Company found with id: {}", company.getId());
        return new GetCompanyByIdUseCaseOut(toCompanyFound(company));
    }

    private void validateId(java.util.UUID id) {
        if (id == null) {
            throw new ValidationException("id", null, "company.id.required");
        }
    }

    private GetCompanyByIdUseCaseOut.CompanyFound toCompanyFound(Company company) {
        return new GetCompanyByIdUseCaseOut.CompanyFound(
                company.getId(),
                company.getNit(),
                company.getLegalName(),
                company.isPayrollTaxExempt(),
                company.isActive()
        );
    }
}
