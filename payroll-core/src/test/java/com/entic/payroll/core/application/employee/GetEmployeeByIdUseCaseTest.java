package com.entic.payroll.core.application.employee;

import com.entic.payroll.core.application.impl.GetEmployeeByIdUseCaseImpl;
import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseOut;
import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetEmployeeByIdUseCaseTest {

    private final EmployeeRepositoryPort repositoryPort = mock(EmployeeRepositoryPort.class);
    private final GetEmployeeByIdUseCaseImpl service = new GetEmployeeByIdUseCaseImpl(repositoryPort);

    @Test
    void shouldGetEmployeeByIdSuccessfully() {
        Long id = 1L;
        UUID companyId = UUID.randomUUID();
        GetEmployeeByIdUseCaseIn request = new GetEmployeeByIdUseCaseIn(id);

        Employee employee = Employee.reconstitute(id, companyId, DocumentType.CITIZENSHIP_CARD,
                "1002003004", "Juan", "Perez", WorkerType.DEPENDENT);
        when(repositoryPort.findById(id)).thenReturn(employee);

        GetEmployeeByIdUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.employeeFound());
        assertEquals(id, result.employeeFound().id());
        assertEquals(companyId, result.employeeFound().companyId());
        assertEquals(DocumentType.CITIZENSHIP_CARD, result.employeeFound().documentType());
        assertEquals("1002003004", result.employeeFound().documentNumber());
        assertEquals("Juan", result.employeeFound().firstNames());
        assertEquals("Perez", result.employeeFound().lastNames());
        assertEquals(WorkerType.DEPENDENT, result.employeeFound().workerType());
        verify(repositoryPort).findById(id);
    }

    @Test
    void shouldFailWhenIdIsNull() {
        GetEmployeeByIdUseCaseIn request = new GetEmployeeByIdUseCaseIn(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("employee.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenEmployeeNotFound() {
        Long id = 1L;
        GetEmployeeByIdUseCaseIn request = new GetEmployeeByIdUseCaseIn(id);

        when(repositoryPort.findById(id)).thenThrow(new NotFoundException("id", id.toString(), "employee.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("employee.notFound", ex.getMessage());
        verify(repositoryPort).findById(id);
    }
}