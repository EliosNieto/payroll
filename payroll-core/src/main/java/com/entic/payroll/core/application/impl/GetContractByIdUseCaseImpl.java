package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.GetContractByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetContractByIdUseCaseOut;
import com.entic.payroll.core.application.port.in.GetContractByIdUseCase;
import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetContractByIdUseCaseImpl implements GetContractByIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetContractByIdUseCaseImpl.class);

    private final ContractRepositoryPort repositoryPort;

    public GetContractByIdUseCaseImpl(ContractRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GetContractByIdUseCaseOut execute(GetContractByIdUseCaseIn request) {
        log.info("Getting contract by id: {}", request.id());

        validateId(request.id());

        Contract contract = repositoryPort.findById(request.id());

        log.info("Contract found with id: {}", contract.getId());
        return new GetContractByIdUseCaseOut(toContractFound(contract));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "contract.id.required");
        }
    }

    private GetContractByIdUseCaseOut.ContractFound toContractFound(Contract contract) {
        return new GetContractByIdUseCaseOut.ContractFound(
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
