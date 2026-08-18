package com.entic.payroll.persistence.adapter.out;

import com.entic.payroll.core.application.company.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.persistence.mapper.CompanyMapper;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyPersistenceAdapter implements CompanyRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(CompanyPersistenceAdapter.class);

    private final CompanyJpaRepository repository;

    public CompanyPersistenceAdapter(CompanyJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Company save(Company company) {
        log.debug("Saving company with NIT: {}", company.getNit());
        com.entic.payroll.persistence.entity.Company entity = CompanyMapper.toEntity(company);
        com.entic.payroll.persistence.entity.Company saved = repository.save(entity);
        return CompanyMapper.toDomain(saved);
    }

    @Override
    public boolean existsByNit(String nit) {
        log.debug("Checking if company exists with NIT: {}", nit);
        return repository.existsByNit(nit);
    }
}
