package com.entic.payroll.core.application.company;

import com.entic.payroll.core.application.impl.CreateCompanyUseCaseImpl;
import com.entic.payroll.core.application.models.CreateCompanyUseCaseIn;
import com.entic.payroll.core.application.models.CreateCompanyUseCaseOut;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCompanyServiceTest {

    private final CompanyRepositoryPort repositoryPort = mock(CompanyRepositoryPort.class);
    private final CreateCompanyUseCaseImpl service = new CreateCompanyUseCaseImpl(repositoryPort);

    @Test
    void shouldCreateCompanySuccessfully() {
        CreateCompanyUseCaseIn command = new CreateCompanyUseCaseIn("900123456", "Acme Corp", false);

        when(repositoryPort.existsByNit("900123456")).thenReturn(false);
        when(repositoryPort.save(any(Company.class))).thenAnswer(invocation -> {
            Company c = invocation.getArgument(0);
            return Company.reconstitute(
                    UUID.randomUUID(),
                    c.getNit(),
                    c.getLegalName(),
                    c.isPayrollTaxExempt(),
                    c.isActive()
            );
        });

        CreateCompanyUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNotNull(result.companyCreated());
        assertNotNull(result.companyCreated().id());
        assertEquals("900123456", result.companyCreated().nit());
        assertEquals("Acme Corp", result.companyCreated().legalName());
        assertFalse(result.companyCreated().payrollTaxExempt());
        assertTrue(result.companyCreated().active());
        verify(repositoryPort).save(any(Company.class));
    }

    @Test
    void shouldFailWhenNitIsNull() {
        CreateCompanyUseCaseIn command = new CreateCompanyUseCaseIn(null, "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.nit.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNitIsBlank() {
        CreateCompanyUseCaseIn command = new CreateCompanyUseCaseIn("  ", "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.nit.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenLegalNameIsNull() {
        CreateCompanyUseCaseIn command = new CreateCompanyUseCaseIn("900123456", null, false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.legalName.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenLegalNameIsBlank() {
        CreateCompanyUseCaseIn command = new CreateCompanyUseCaseIn("900123456", "  ", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("company.legalName.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNitAlreadyExists() {
        CreateCompanyUseCaseIn command = new CreateCompanyUseCaseIn("900123456", "Acme Corp", false);

        when(repositoryPort.existsByNit("900123456")).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(command));
        assertEquals("company.nit.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
