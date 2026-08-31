package com.entic.payroll.api.infrastructure.mapper;

import com.entic.payroll.api.infrastructure.dto.contract.ContractResponse;
import com.entic.payroll.api.infrastructure.dto.contract.CreateContractRequest;
import com.entic.payroll.api.infrastructure.dto.contract.UpdateContractRequest;
import com.entic.payroll.core.application.models.CreateContractUseCaseIn;
import com.entic.payroll.core.application.models.UpdateContractUseCaseIn;
import com.entic.payroll.core.domain.contract.Contract;

public final class ContractApiMapper {

    private ContractApiMapper() {}

    public static CreateContractUseCaseIn toCommand(CreateContractRequest request) {
        return new CreateContractUseCaseIn(
                request.companyId(),
                request.employeeId(),
                request.contractType(),
                request.baseSalary(),
                request.integralSalary(),
                request.riskLevelArl(),
                request.startDate(),
                request.endDate()
        );
    }

    public static UpdateContractUseCaseIn toCommand(Long id, UpdateContractRequest request) {
        return new UpdateContractUseCaseIn(
                id,
                request.contractType(),
                request.baseSalary(),
                request.integralSalary(),
                request.riskLevelArl(),
                request.startDate(),
                request.endDate()
        );
    }

    public static ContractResponse toResponse(Contract contract) {
        return new ContractResponse(
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
