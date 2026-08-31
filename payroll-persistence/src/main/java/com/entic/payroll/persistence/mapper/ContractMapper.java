package com.entic.payroll.persistence.mapper;

import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.persistence.entity.Company;
import com.entic.payroll.persistence.entity.Employee;

public final class ContractMapper {

    private ContractMapper() {}

    public static Contract toDomain(com.entic.payroll.persistence.entity.Contract entity) {
        return Contract.reconstitute(
                entity.getId(),
                entity.getCompany().getId(),
                entity.getEmployee().getId(),
                ContractType.fromCode(entity.getContractType()),
                entity.getBaseSalary(),
                entity.isIntegralSalary(),
                entity.getRiskLevelArl(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }

    public static com.entic.payroll.persistence.entity.Contract toEntity(
            Contract domain,
            Company company,
            Employee employee) {
        com.entic.payroll.persistence.entity.Contract entity =
                new com.entic.payroll.persistence.entity.Contract();
        entity.setId(domain.getId());
        entity.setCompany(company);
        entity.setEmployee(employee);
        entity.setContractType(domain.getContractType().getCode());
        entity.setBaseSalary(domain.getBaseSalary());
        entity.setIntegralSalary(domain.isIntegralSalary());
        entity.setRiskLevelArl(domain.getRiskLevelArl());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        return entity;
    }
}
