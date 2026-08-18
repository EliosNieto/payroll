package com.entic.payroll.persistence.mapper;

import com.entic.payroll.core.domain.company.Company;

public final class CompanyMapper {

    private CompanyMapper() {}

    public static Company toDomain(com.entic.payroll.persistence.entity.Company entity) {
        return Company.reconstitute(
                entity.getId(),
                entity.getNit(),
                entity.getLegalName(),
                entity.isPayrollTaxExempt(),
                entity.isActive()
        );
    }

    public static com.entic.payroll.persistence.entity.Company toEntity(Company domain) {
        com.entic.payroll.persistence.entity.Company entity = new com.entic.payroll.persistence.entity.Company();
        entity.setId(domain.getId());
        entity.setNit(domain.getNit());
        entity.setLegalName(domain.getLegalName());
        entity.setPayrollTaxExempt(domain.isPayrollTaxExempt());
        entity.setActive(domain.isActive());
        return entity;
    }
}
