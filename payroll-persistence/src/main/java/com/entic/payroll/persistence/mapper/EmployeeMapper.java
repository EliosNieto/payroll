package com.entic.payroll.persistence.mapper;

import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;

public final class EmployeeMapper {

    private EmployeeMapper() {}

    public static Employee toDomain(com.entic.payroll.persistence.entity.Employee entity) {
        return Employee.reconstitute(
                entity.getId(),
                entity.getCompany().getId(),
                DocumentType.fromCode(entity.getDocumentType()),
                entity.getDocumentNumber(),
                entity.getFirstNames(),
                entity.getLastNames(),
                WorkerType.fromCode(entity.getWorkerType())
        );
    }

    public static com.entic.payroll.persistence.entity.Employee toEntity(
            Employee domain,
            com.entic.payroll.persistence.entity.Company company) {
        com.entic.payroll.persistence.entity.Employee entity = new com.entic.payroll.persistence.entity.Employee();
        entity.setId(domain.getId());
        entity.setCompany(company);
        entity.setDocumentType(domain.getDocumentType().getCode());
        entity.setDocumentNumber(domain.getDocumentNumber());
        entity.setFirstNames(domain.getFirstNames());
        entity.setLastNames(domain.getLastNames());
        entity.setWorkerType(domain.getWorkerType().getCode());
        return entity;
    }
}