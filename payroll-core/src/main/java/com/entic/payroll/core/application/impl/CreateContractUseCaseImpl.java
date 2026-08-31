package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.CreateContractUseCaseIn;
import com.entic.payroll.core.application.models.CreateContractUseCaseOut;
import com.entic.payroll.core.application.port.in.CreateContractUseCase;
import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.core.domain.enums.RiskLevel;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateContractUseCaseImpl implements CreateContractUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateContractUseCaseImpl.class);

    private final ContractRepositoryPort repositoryPort;

    public CreateContractUseCaseImpl(ContractRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CreateContractUseCaseOut execute(CreateContractUseCaseIn request) {
        log.info("Creating contract for employee: {} in company: {}", request.employeeId(), request.companyId());

        validateCompanyId(request.companyId());
        validateEmployeeId(request.employeeId());
        validateCompanyExists(request.companyId());
        validateEmployeeExists(request.employeeId());
        validateContractType(request.contractType());
        validateBaseSalary(request.baseSalary());
        validateRiskLevel(request.riskLevelArl());
        validateStartDate(request.startDate());
        validateDateRange(request.startDate(), request.endDate());
        validateNoActiveContract(request.employeeId(), null);

        Contract contract = Contract.create(request.companyId(), request.employeeId(), request.contractType(),
                request.baseSalary(), request.integralSalary(), request.riskLevelArl(),
                request.startDate(), request.endDate());
        Contract saved = repositoryPort.save(contract);

        log.info("Contract created with id: {}", saved.getId());
        return new CreateContractUseCaseOut(toContractCreated(saved));
    }

    private void validateCompanyId(UUID companyId) {
        if (companyId == null) {
            throw new ValidationException("companyId", null, "contract.companyId.required");
        }
    }

    private void validateEmployeeId(Long employeeId) {
        if (employeeId == null) {
            throw new ValidationException("employeeId", null, "contract.employeeId.required");
        }
    }

    private void validateCompanyExists(UUID companyId) {
        if (!repositoryPort.companyExists(companyId)) {
            throw new NotFoundException("companyId", companyId.toString(), "contract.company.notFound");
        }
    }

    private void validateEmployeeExists(Long employeeId) {
        if (!repositoryPort.employeeExists(employeeId)) {
            throw new NotFoundException("employeeId", employeeId.toString(), "contract.employee.notFound");
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

    private CreateContractUseCaseOut.ContractCreated toContractCreated(Contract contract) {
        return new CreateContractUseCaseOut.ContractCreated(
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
