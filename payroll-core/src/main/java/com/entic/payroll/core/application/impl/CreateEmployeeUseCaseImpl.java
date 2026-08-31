package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.CreateEmployeeUseCaseIn;
import com.entic.payroll.core.application.models.CreateEmployeeUseCaseOut;
import com.entic.payroll.core.application.port.in.CreateEmployeeUseCase;
import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CreateEmployeeUseCaseImpl implements CreateEmployeeUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateEmployeeUseCaseImpl.class);

    private final EmployeeRepositoryPort repositoryPort;

    public CreateEmployeeUseCaseImpl(EmployeeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CreateEmployeeUseCaseOut execute(CreateEmployeeUseCaseIn request) {
        log.info("Creating employee with document number: {} in company: {}", request.documentNumber(), request.companyId());

        validateCompanyId(request.companyId());
        validateCompanyExists(request.companyId());
        validateDocumentType(request.documentType());
        validateDocumentNumber(request.documentNumber());
        validateFirstNames(request.firstNames());
        validateLastNames(request.lastNames());
        validateWorkerType(request.workerType());
        validateDocumentNumberUnique(request.companyId(), request.documentNumber());

        Employee employee = Employee.create(request.companyId(), request.documentType(), request.documentNumber(),
                request.firstNames(), request.lastNames(), request.workerType());
        Employee saved = repositoryPort.save(employee);

        log.info("Employee created with id: {}", saved.getId());
        return new CreateEmployeeUseCaseOut(toEmployeeCreated(saved));
    }

    private void validateCompanyId(UUID companyId) {
        if (companyId == null) {
            throw new ValidationException("companyId", null, "employee.companyId.required");
        }
    }

    private void validateCompanyExists(UUID companyId) {
        if (!repositoryPort.companyExists(companyId)) {
            throw new NotFoundException("companyId", companyId.toString(), "employee.company.notFound");
        }
    }

    private void validateDocumentType(DocumentType documentType) {
        if (documentType == null) {
            throw new ValidationException("documentType", null, "employee.documentType.required");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new ValidationException("documentNumber", documentNumber, "employee.documentNumber.required");
        }
    }

    private void validateFirstNames(String firstNames) {
        if (firstNames == null || firstNames.isBlank()) {
            throw new ValidationException("firstNames", firstNames, "employee.firstNames.required");
        }
    }

    private void validateLastNames(String lastNames) {
        if (lastNames == null || lastNames.isBlank()) {
            throw new ValidationException("lastNames", lastNames, "employee.lastNames.required");
        }
    }

    private void validateWorkerType(WorkerType workerType) {
        if (workerType == null) {
            throw new ValidationException("workerType", null, "employee.workerType.required");
        }
    }

    private void validateDocumentNumberUnique(UUID companyId, String documentNumber) {
        if (repositoryPort.existsByCompanyIdAndDocumentNumber(companyId, documentNumber)) {
            throw new AlreadyExistsException("documentNumber", documentNumber, "employee.documentNumber.alreadyExists");
        }
    }

    private CreateEmployeeUseCaseOut.EmployeeCreated toEmployeeCreated(Employee employee) {
        return new CreateEmployeeUseCaseOut.EmployeeCreated(
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