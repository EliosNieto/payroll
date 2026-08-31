package com.entic.payroll.core.application.port.out;

import com.entic.payroll.core.domain.contract.Contract;

import java.util.UUID;

public interface ContractRepositoryPort {

    Contract save(Contract contract);

    Contract findById(Long id);

    boolean companyExists(UUID companyId);

    boolean employeeExists(Long employeeId);

    boolean existsActiveForEmployee(Long employeeId, Long excludingId);
}
