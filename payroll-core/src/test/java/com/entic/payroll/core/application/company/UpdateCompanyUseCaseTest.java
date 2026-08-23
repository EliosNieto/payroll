package com.entic.payroll.core.application.company;

import com.entic.payroll.core.application.impl.UpdateCompanyUseCaseImpl;
import com.entic.payroll.core.application.models.UpdateCompanyUseCaseIn;
import com.entic.payroll.core.application.models.UpdateCompanyUseCaseOut;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCompanyUseCaseTest {

    private final CompanyRepositoryPort repositoryPort = mock(CompanyRepositoryPort.class);
    private final UpdateCompanyUseCaseImpl service = new UpdateCompanyUseCaseImpl(repositoryPort);

    @Test
    void shouldUpdateCompanySuccessfully() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, "900123456", "Acme Corp Updated", true);

        Company existing = Company.reconstitute(id, "900000000", "Acme Corp", false, true);
        when(repositoryPort.findById(id)).thenReturn(existing);
        when(repositoryPort.existsByNitExcludingId("900123456", id)).thenReturn(false);
        when(repositoryPort.save(any(Company.class))).thenAnswer(invocation -> {
            Company c = invocation.getArgument(0);
            return c;
        });

        UpdateCompanyUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.companyUpdated());
        assertEquals(id, result.companyUpdated().id());
        assertEquals("900123456", result.companyUpdated().nit());
        assertEquals("Acme Corp Updated", result.companyUpdated().legalName());
        assertTrue(result.companyUpdated().payrollTaxExempt());
        assertTrue(result.companyUpdated().active());
        verify(repositoryPort).save(any(Company.class));
    }

    @Test
    void shouldFailWhenIdIsNull() {
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(null, "900123456", "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("company.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenNitIsNull() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, null, "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("company.nit.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenNitIsBlank() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, "  ", "Acme Corp", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("company.nit.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenLegalNameIsNull() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, "900123456", null, false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("company.legalName.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenLegalNameIsBlank() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, "900123456", "  ", false);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("company.legalName.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenCompanyNotFound() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, "900123456", "Acme Corp", false);

        when(repositoryPort.findById(id)).thenThrow(new NotFoundException("id", id.toString(), "company.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("company.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNitAlreadyExistsInAnotherCompany() {
        UUID id = UUID.randomUUID();
        UpdateCompanyUseCaseIn request = new UpdateCompanyUseCaseIn(id, "900123456", "Acme Corp", false);

        Company existing = Company.reconstitute(id, "900000000", "Acme Corp", false, true);
        when(repositoryPort.findById(id)).thenReturn(existing);
        when(repositoryPort.existsByNitExcludingId("900123456", id)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(request));
        assertEquals("company.nit.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
