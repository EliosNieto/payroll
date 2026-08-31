package com.entic.payroll.persistence.repository;

import com.entic.payroll.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {

    boolean existsByCompanyId(UUID companyId);

    boolean existsByCompanyIdAndDocumentNumber(UUID companyId, String documentNumber);

    boolean existsByCompanyIdAndDocumentNumberAndIdNot(UUID companyId, String documentNumber, Long id);
}