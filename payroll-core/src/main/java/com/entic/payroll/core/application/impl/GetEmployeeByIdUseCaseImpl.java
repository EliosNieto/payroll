package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseOut;
import com.entic.payroll.core.application.port.in.GetEmployeeByIdUseCase;
import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetEmployeeByIdUseCaseImpl implements GetEmployeeByIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetEmployeeByIdUseCaseImpl.class);

    private final EmployeeRepositoryPort repositoryPort;

    public GetEmployeeByIdUseCaseImpl(EmployeeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GetEmployeeByIdUseCaseOut execute(GetEmployeeByIdUseCaseIn request) {
        log.info("Getting employee by id: {}", request.id());

        validateId(request.id());

        Employee employee = repositoryPort.findById(request.id());

        log.info("Employee found with id: {}", employee.getId());
        return new GetEmployeeByIdUseCaseOut(toEmployeeFound(employee));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "employee.id.required");
        }
    }

    private GetEmployeeByIdUseCaseOut.EmployeeFound toEmployeeFound(Employee employee) {
        return new GetEmployeeByIdUseCaseOut.EmployeeFound(
                employee.getId(),
                employee.getCompanyId(),
                employee.getDocumentType(),
                employee.getDocumentNumber(),
                employee.getFirstNames(),
                employee.getLastNames(),
                employee.getWorkerType()
        );
    }
}