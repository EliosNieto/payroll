package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.UpdateCompanyUseCaseIn;
import com.entic.payroll.core.application.models.UpdateCompanyUseCaseOut;
import com.entic.payroll.core.application.port.in.UpdateCompanyUseCase;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateCompanyUseCaseImpl implements UpdateCompanyUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateCompanyUseCaseImpl.class);

    private final CompanyRepositoryPort repositoryPort;

    public UpdateCompanyUseCaseImpl(CompanyRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UpdateCompanyUseCaseOut execute(UpdateCompanyUseCaseIn request) {
        log.info("Updating company with id: {}", request.id());

        validateId(request.id());
        validateNit(request.nit());
        validateLegalName(request.legalName());

        Company company = repositoryPort.findById(request.id());
        validateNitUniqueExcluding(request.nit(), request.id());

        Company updated = company.update(request.nit(), request.legalName(), request.payrollTaxExempt());
        Company saved = repositoryPort.save(updated);

        log.info("Company updated with id: {}", saved.getId());
        return new UpdateCompanyUseCaseOut(toCompanyUpdated(saved));
    }

    private void validateId(java.util.UUID id) {
        if (id == null) {
            throw new ValidationException("id", null, "company.id.required");
        }
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

    private void validateNitUniqueExcluding(String nit, java.util.UUID id) {
        if (repositoryPort.existsByNitExcludingId(nit, id)) {
            throw new AlreadyExistsException("nit", nit, "company.nit.alreadyExists");
        }
    }

    private UpdateCompanyUseCaseOut.CompanyUpdated toCompanyUpdated(Company company) {
        return new UpdateCompanyUseCaseOut.CompanyUpdated(
                company.getId(),
                company.getNit(),
                company.getLegalName(),
                company.isPayrollTaxExempt(),
                company.isActive()
        );
    }
}
