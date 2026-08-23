package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.CreateCompanyUseCaseIn;
import com.entic.payroll.core.application.models.CreateCompanyUseCaseOut;
import com.entic.payroll.core.application.port.in.CreateCompanyUseCase;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateCompanyUseCaseImpl implements CreateCompanyUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateCompanyUseCaseImpl.class);

    private final CompanyRepositoryPort repositoryPort;

    public CreateCompanyUseCaseImpl(CompanyRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CreateCompanyUseCaseOut execute(CreateCompanyUseCaseIn command) {
        log.info("Creating company with NIT: {}", command.nit());

        validateNit(command.nit());
        validateLegalName(command.legalName());
        validateNitUnique(command.nit());

        Company company = Company.create(command.nit(), command.legalName(), command.payrollTaxExempt());
        Company saved = repositoryPort.save(company);

        log.info("Company created with id: {}", saved.getId());
        return new CreateCompanyUseCaseOut(toCompanyCreated(saved));
    }

    private void validateNit(String nit) {
        if (nit == null || nit.isBlank()) {
            throw new ValidationException("nit", nit, "company.nit.required");
        }
    }

    private void validateLegalName(String legalName) {
        if (legalName == null || legalName.isBlank()) {
            throw new ValidationException("legalName", legalName, "company.legalName.required");
        }
    }

    private void validateNitUnique(String nit) {
        if (repositoryPort.existsByNit(nit)) {
            throw new AlreadyExistsException("nit", nit, "company.nit.alreadyExists");
        }
    }

    private CreateCompanyUseCaseOut.CompanyCreated toCompanyCreated(Company company){
        return new CreateCompanyUseCaseOut.CompanyCreated(company.getId(), company.getNit(), company.getLegalName(), company.isPayrollTaxExempt(), company.isActive());
    }
}
