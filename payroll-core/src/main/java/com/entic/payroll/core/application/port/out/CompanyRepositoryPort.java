package com.entic.payroll.core.application.port.out;

import com.entic.payroll.core.domain.company.Company;

public interface CompanyRepositoryPort {

    Company save(Company company);

    Company findById(java.util.UUID id);

    boolean existsByNit(String nit);

    boolean existsByNitExcludingId(String nit, java.util.UUID id);
}
