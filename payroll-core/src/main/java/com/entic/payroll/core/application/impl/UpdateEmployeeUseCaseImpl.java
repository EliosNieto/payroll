package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.UpdateEmployeeUseCaseIn;
import com.entic.payroll.core.application.models.UpdateEmployeeUseCaseOut;
import com.entic.payroll.core.application.port.in.UpdateEmployeeUseCase;
import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class UpdateEmployeeUseCaseImpl implements UpdateEmployeeUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateEmployeeUseCaseImpl.class);

    private final EmployeeRepositoryPort repositoryPort;

    public UpdateEmployeeUseCaseImpl(EmployeeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UpdateEmployeeUseCaseOut execute(UpdateEmployeeUseCaseIn request) {
        log.info("Updating employee with id: {}", request.id());

        validateId(request.id());
        validateDocumentType(request.documentType());
        validateDocumentNumber(request.documentNumber());
        validateFirstNames(request.firstNames());
        validateLastNames(request.lastNames());
        validateWorkerType(request.workerType());

        Employee employee = repositoryPort.findById(request.id());
        validateDocumentNumberUniqueExcluding(employee.getCompanyId(), request.documentNumber(), request.id());

        Employee updated = employee.update(request.documentType(), request.documentNumber(),
                request.firstNames(), request.lastNames(), request.workerType());
        Employee saved = repositoryPort.save(updated);

        log.info("Employee updated with id: {}", saved.getId());
        return new UpdateEmployeeUseCaseOut(toEmployeeUpdated(saved));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "employee.id.required");
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

    private void validateDocumentNumberUniqueExcluding(UUID companyId, String documentNumber, Long id) {
        if (repositoryPort.existsByCompanyIdAndDocumentNumberExcludingId(companyId, documentNumber, id)) {
            throw new AlreadyExistsException("documentNumber", documentNumber, "employee.documentNumber.alreadyExists");
        }
    }

    private UpdateEmployeeUseCaseOut.EmployeeUpdated toEmployeeUpdated(Employee employee) {
        return new UpdateEmployeeUseCaseOut.EmployeeUpdated(
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