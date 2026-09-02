package com.entic.payroll.persistence.mapper;

import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import com.entic.payroll.persistence.entity.Company;

public final class PaymentConceptMapper {

    private PaymentConceptMapper() {}

    public static PaymentConcept toDomain(com.entic.payroll.persistence.entity.PaymentConcept entity) {
        Company company = entity.getCompany();
        return PaymentConcept.reconstitute(
                entity.getId(),
                company != null ? company.getId() : null,
                entity.getCode(),
                entity.getName(),
                entity.getNature()
        );
    }

    public static com.entic.payroll.persistence.entity.PaymentConcept toEntity(
            PaymentConcept domain,
            Company company) {
        com.entic.payroll.persistence.entity.PaymentConcept entity =
                new com.entic.payroll.persistence.entity.PaymentConcept();
        entity.setId(domain.getId());
        entity.setCompany(company);
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setNature(domain.getNature());
        return entity;
    }
}
