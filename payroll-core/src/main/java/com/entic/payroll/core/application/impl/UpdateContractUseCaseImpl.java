package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.UpdateContractUseCaseIn;
import com.entic.payroll.core.application.models.UpdateContractUseCaseOut;
import com.entic.payroll.core.application.port.in.UpdateContractUseCase;
import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.core.domain.enums.RiskLevel;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateContractUseCaseImpl implements UpdateContractUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateContractUseCaseImpl.class);

    private final ContractRepositoryPort repositoryPort;

    public UpdateContractUseCaseImpl(ContractRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UpdateContractUseCaseOut execute(UpdateContractUseCaseIn request) {
        log.info("Updating contract with id: {}", request.id());

        validateId(request.id());
        validateContractType(request.contractType());
        validateBaseSalary(request.baseSalary());
        validateRiskLevel(request.riskLevelArl());
        validateStartDate(request.startDate());
        validateDateRange(request.startDate(), request.endDate());

        Contract existing = repositoryPort.findById(request.id());
        if (existing.isActive() && request.endDate() == null) {
            validateNoActiveContract(existing.getEmployeeId(), request.id());
        }

        Contract updated = existing.update(request.contractType(), request.baseSalary(), request.integralSalary(),
                request.riskLevelArl(), request.startDate(), request.endDate());
        Contract saved = repositoryPort.save(updated);

        log.info("Contract updated with id: {}", saved.getId());
        return new UpdateContractUseCaseOut(toContractUpdated(saved));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "contract.id.required");
        }
    }

    private void validateContractType(ContractType contractType) {
        if (contractType == null) {
            throw new ValidationException("contractType", null, "contract.contractType.required");
        }
    }

    private void validateBaseSalary(BigDecimal baseSalary) {
        if (baseSalary == null) {
            throw new ValidationException("baseSalary", null, "contract.baseSalary.required");
        }
        if (baseSalary.signum() <= 0) {
            throw new ValidationException("baseSalary", baseSalary.toPlainString(), "contract.baseSalary.invalid");
        }
    }

    private void validateRiskLevel(RiskLevel riskLevelArl) {
        if (riskLevelArl == null) {
            throw new ValidationException("riskLevelArl", null, "contract.riskLevelArl.required");
        }
    }

    private void validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new ValidationException("startDate", null, "contract.startDate.required");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new ValidationException("endDate", endDate.toString(), "contract.dateRange.invalid");
        }
    }

    private void validateNoActiveContract(Long employeeId, Long excludingId) {
        if (repositoryPort.existsActiveForEmployee(employeeId, excludingId)) {
            throw new AlreadyExistsException("employeeId", employeeId.toString(), "contract.active.alreadyExists");
        }
    }

    private UpdateContractUseCaseOut.ContractUpdated toContractUpdated(Contract contract) {
        return new UpdateContractUseCaseOut.ContractUpdated(
                contract.getId(),
                contract.getCompanyId(),
                contract.getEmployeeId(),
                contract.getContractType(),
                contract.getBaseSalary(),
                contract.isIntegralSalary(),
                contract.getRiskLevelArl(),
                contract.getStartDate(),
                contract.getEndDate()
        );
    }
}
