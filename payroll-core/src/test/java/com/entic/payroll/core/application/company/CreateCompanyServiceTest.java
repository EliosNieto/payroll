package com.entic.payroll.core.application.company;

import com.entic.payroll.core.application.company.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCompanyServiceTest {

    private final CompanyRepositoryPort repositoryPort = mock(CompanyRepositoryPort.class);
    private final CreateCompanyService service = new CreateCompanyService(repositoryPort);

    @Test
    void shouldCreateCompanySuccessfully() {
        CreateCompanyCommand command = new CreateCompanyCommand("900123456", "Acme Corp", false);

        when(repositoryPort.existsByNit("900123456")).thenReturn(false);
        when(repositoryPort.save(any(Company.class))).thenAnswer(invocation -> {
            Company c = invocation.getArgument(0);
            return Company.reconstitute(
                    java.util.UUID.randomUUID(),
                    c.getNit(),
                    c.getLegalName(),
                    c.isPayrollTaxExempt(),
                    c.isActive()
            );
        });

        Company result = service.execute(command);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("900123456", result.getNit());
        assertEquals("Acme Corp", result.getLegalName());
        assertFalse(result.isPayrollTaxExempt());
        assertTrue(result.isActive());
        verify(repositoryPort).save(any(Company.class));
    }

    @Test
    void shouldFailWhenNitIsNull() {
        CreateCompanyCommand command = new CreateCompanyCommand(null, "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.nit.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNitIsBlank() {
        CreateCompanyCommand command = new CreateCompanyCommand("  ", "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.nit.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenLegalNameIsNull() {
        CreateCompanyCommand command = new CreateCompanyCommand("900123456", null, false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.legalName.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenLegalNameIsBlank() {
        CreateCompanyCommand command = new CreateCompanyCommand("900123456", "  ", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.legalName.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNitAlreadyExists() {
        CreateCompanyCommand command = new CreateCompanyCommand("900123456", "Acme Corp", false);

        when(repositoryPort.existsByNit("900123456")).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(command));
        assertEquals("company.nit.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
