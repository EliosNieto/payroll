package com.entic.payroll.core.application.employee;

import com.entic.payroll.core.application.impl.UpdateEmployeeUseCaseImpl;
import com.entic.payroll.core.application.models.UpdateEmployeeUseCaseIn;
import com.entic.payroll.core.application.models.UpdateEmployeeUseCaseOut;
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

class UpdateEmployeeUseCaseTest {

    private final EmployeeRepositoryPort repositoryPort = mock(EmployeeRepositoryPort.class);
    private final UpdateEmployeeUseCaseImpl service = new UpdateEmployeeUseCaseImpl(repositoryPort);

    private static final UUID COMPANY_ID = UUID.randomUUID();

    private UpdateEmployeeUseCaseIn validCommand(Long id) {
        return new UpdateEmployeeUseCaseIn(
                id,
                DocumentType.CITIZENSHIP_CARD,
                "1002003004",
                "Juan",
                "Perez",
                WorkerType.DEPENDENT
        );
    }

    @Test
    void shouldUpdateEmployeeSuccessfully() {
        Long id = 1L;
        UpdateEmployeeUseCaseIn request = validCommand(id);

        Employee existing = Employee.reconstitute(id, COMPANY_ID, DocumentType.CITIZENSHIP_CARD,
                "900000000", "Maria", "Gomez", WorkerType.INDEPENDENT);
        when(repositoryPort.findById(id)).thenReturn(existing);
        when(repositoryPort.existsByCompanyIdAndDocumentNumberExcludingId(COMPANY_ID, "1002003004", id)).thenReturn(false);
        when(repositoryPort.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateEmployeeUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.employeeUpdated());
        assertEquals(id, result.employeeUpdated().id());
        assertEquals(COMPANY_ID, result.employeeUpdated().companyId());
        assertEquals(DocumentType.CITIZENSHIP_CARD, result.employeeUpdated().documentType());
        assertEquals("1002003004", result.employeeUpdated().documentNumber());
        assertEquals("Juan", result.employeeUpdated().firstNames());
        assertEquals("Perez", result.employeeUpdated().lastNames());
        assertEquals(WorkerType.DEPENDENT, result.employeeUpdated().workerType());
        verify(repositoryPort).save(any(Employee.class));
    }

    @Test
    void shouldFailWhenIdIsNull() {
        UpdateEmployeeUseCaseIn request = validCommand(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenDocumentTypeIsNull() {
        UpdateEmployeeUseCaseIn request = new UpdateEmployeeUseCaseIn(1L, null, "1002003004",
                "Juan", "Perez", WorkerType.DEPENDENT);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.documentType.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenDocumentNumberIsNull() {
        UpdateEmployeeUseCaseIn request = new UpdateEmployeeUseCaseIn(1L, DocumentType.CITIZENSHIP_CARD,
                null, "Juan", "Perez", WorkerType.DEPENDENT);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.documentNumber.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenFirstNamesIsBlank() {
        UpdateEmployeeUseCaseIn request = new UpdateEmployeeUseCaseIn(1L, DocumentType.CITIZENSHIP_CARD,
                "1002003004", "  ", "Perez", WorkerType.DEPENDENT);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.firstNames.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenLastNamesIsBlank() {
        UpdateEmployeeUseCaseIn request = new UpdateEmployeeUseCaseIn(1L, DocumentType.CITIZENSHIP_CARD,
                "1002003004", "Juan", " ", WorkerType.DEPENDENT);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.lastNames.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenWorkerTypeIsNull() {
        UpdateEmployeeUseCaseIn request = new UpdateEmployeeUseCaseIn(1L, DocumentType.CITIZENSHIP_CARD,
                "1002003004", "Juan", "Perez", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.workerType.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenEmployeeNotFound() {
        Long id = 1L;
        UpdateEmployeeUseCaseIn request = validCommand(id);

        when(repositoryPort.findById(id)).thenThrow(new NotFoundException("id", id.toString(), "employee.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("employee.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDocumentNumberAlreadyExistsInAnotherRecord() {
        Long id = 1L;
        UpdateEmployeeUseCaseIn request = validCommand(id);

        Employee existing = Employee.reconstitute(id, COMPANY_ID, DocumentType.CITIZENSHIP_CARD,
                "900000000", "Maria", "Gomez", WorkerType.INDEPENDENT);
        when(repositoryPort.findById(id)).thenReturn(existing);
        when(repositoryPort.existsByCompanyIdAndDocumentNumberExcludingId(COMPANY_ID, "1002003004", id)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(request));
        assertEquals("employee.documentNumber.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}