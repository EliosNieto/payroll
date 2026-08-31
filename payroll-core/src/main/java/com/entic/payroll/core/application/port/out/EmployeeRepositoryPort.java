package com.entic.payroll.core.application.port.out;

import com.entic.payroll.core.domain.employee.Employee;

import java.util.UUID;

public interface EmployeeRepositoryPort {

    Employee save(Employee employee);

    Employee findById(Long id);

    boolean companyExists(UUID companyId);

    boolean existsByCompanyIdAndDocumentNumber(UUID companyId, String documentNumber);

    boolean existsByCompanyIdAndDocumentNumberExcludingId(UUID companyId, String documentNumber, Long id);
}