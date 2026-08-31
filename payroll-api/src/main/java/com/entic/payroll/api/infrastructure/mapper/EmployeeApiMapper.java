package com.entic.payroll.api.infrastructure.mapper;

import com.entic.payroll.api.infrastructure.dto.employee.CreateEmployeeRequest;
import com.entic.payroll.api.infrastructure.dto.employee.EmployeeResponse;
import com.entic.payroll.api.infrastructure.dto.employee.UpdateEmployeeRequest;
import com.entic.payroll.core.application.models.CreateEmployeeUseCaseIn;
import com.entic.payroll.core.application.models.UpdateEmployeeUseCaseIn;
import com.entic.payroll.core.domain.employee.Employee;

public final class EmployeeApiMapper {

    private EmployeeApiMapper() {}

    public static CreateEmployeeUseCaseIn toCommand(CreateEmployeeRequest request) {
        return new CreateEmployeeUseCaseIn(
                request.companyId(),
                request.documentType(),
                request.documentNumber(),
                request.firstNames(),
                request.lastNames(),
                request.workerType()
        );
    }

    public static UpdateEmployeeUseCaseIn toCommand(Long id, UpdateEmployeeRequest request) {
        return new UpdateEmployeeUseCaseIn(
                id,
                request.documentType(),
                request.documentNumber(),
                request.firstNames(),
                request.lastNames(),
                request.workerType()
        );
    }

    public static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
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