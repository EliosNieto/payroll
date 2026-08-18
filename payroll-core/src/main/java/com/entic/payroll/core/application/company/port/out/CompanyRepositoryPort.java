package com.entic.payroll.core.application.company.port.out;

import com.entic.payroll.core.domain.company.Company;

public interface CompanyRepositoryPort {

    Company save(Company company);

    boolean existsByNit(String nit);
}
