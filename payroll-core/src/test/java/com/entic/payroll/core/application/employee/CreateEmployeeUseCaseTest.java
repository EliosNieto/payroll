package com.entic.payroll.core.application.employee;

import com.entic.payroll.core.application.impl.CreateEmployeeUseCaseImpl;
import com.entic.payroll.core.application.models.CreateEmployeeUseCaseIn;
import com.entic.payroll.core.application.models.CreateEmployeeUseCaseOut;
import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateEmployeeUseCaseTest {

    private final EmployeeRepositoryPort repositoryPort = mock(EmployeeRepositoryPort.class);
    private final CreateEmployeeUseCaseImpl service = new CreateEmployeeUseCaseImpl(repositoryPort);

    private static final UUID COMPANY_ID = UUID.randomUUID();

    private CreateEmployeeUseCaseIn validCommand() {
        return new CreateEmployeeUseCaseIn(
                COMPANY_ID,
                DocumentType.CITIZENSHIP_CARD,
                "1002003004",
                "Juan",
                "Perez",
                WorkerType.DEPENDENT
        );
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        CreateEmployeeUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.existsByCompanyIdAndDocumentNumber(COMPANY_ID, "1002003004")).thenReturn(false);
        when(repositoryPort.save(any(Employee.class))).thenAnswer(invocation -> {
            Employee e = invocation.getArgument(0);
            return Employee.reconstitute(1L, e.getCompanyId(), e.getDocumentType(), e.getDocumentNumber(),
                    e.getFirstNames(), e.getLastNames(), e.getWorkerType());
        });

        CreateEmployeeUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNotNull(result.employeeCreated());
        assertNotNull(result.employeeCreated().id());
        assertEquals(COMPANY_ID, result.employeeCreated().companyId());
        assertEquals(DocumentType.CITIZENSHIP_CARD, result.employeeCreated().documentType());
        assertEquals("1002003004", result.employeeCreated().documentNumber());
        assertEquals("Juan", result.employeeCreated().firstNames());
        assertEquals("Perez", result.employeeCreated().lastNames());
        assertEquals(WorkerType.DEPENDENT, result.employeeCreated().workerType());
        verify(repositoryPort).save(any(Employee.class));
    }

    @Test
    void shouldFailWhenCompanyIdIsNull() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(null,
                DocumentType.CITIZENSHIP_CARD, "1002003004", "Juan", "Perez", WorkerType.DEPENDENT);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.companyId.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenCompanyNotFound() {
        CreateEmployeeUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(command));
        assertEquals("employee.company.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDocumentTypeIsNull() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(COMPANY_ID,
                null, "1002003004", "Juan", "Perez", WorkerType.DEPENDENT);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.documentType.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDocumentNumberIsNull() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(COMPANY_ID,
                DocumentType.CITIZENSHIP_CARD, null, "Juan", "Perez", WorkerType.DEPENDENT);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.documentNumber.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDocumentNumberIsBlank() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(COMPANY_ID,
                DocumentType.CITIZENSHIP_CARD, "  ", "Juan", "Perez", WorkerType.DEPENDENT);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.documentNumber.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenFirstNamesIsNull() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(COMPANY_ID,
                DocumentType.CITIZENSHIP_CARD, "1002003004", null, "Perez", WorkerType.DEPENDENT);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.firstNames.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenLastNamesIsNull() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(COMPANY_ID,
                DocumentType.CITIZENSHIP_CARD, "1002003004", "Juan", null, WorkerType.DEPENDENT);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.lastNames.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenWorkerTypeIsNull() {
        CreateEmployeeUseCaseIn command = new CreateEmployeeUseCaseIn(COMPANY_ID,
                DocumentType.CITIZENSHIP_CARD, "1002003004", "Juan", "Perez", null);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("employee.workerType.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDocumentNumberAlreadyExists() {
        CreateEmployeeUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.existsByCompanyIdAndDocumentNumber(COMPANY_ID, "1002003004")).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(command));
        assertEquals("employee.documentNumber.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}