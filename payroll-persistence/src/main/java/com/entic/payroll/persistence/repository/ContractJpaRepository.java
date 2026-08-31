package com.entic.payroll.persistence.repository;

import com.entic.payroll.persistence.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractJpaRepository extends JpaRepository<Contract, Long> {

    boolean existsByEmployeeIdAndEndDateIsNull(Long employeeId);

    boolean existsByEmployeeIdAndEndDateIsNullAndIdNot(Long employeeId, Long id);
}
