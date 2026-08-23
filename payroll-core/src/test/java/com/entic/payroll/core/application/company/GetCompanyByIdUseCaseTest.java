package com.entic.payroll.core.application.company;

import com.entic.payroll.core.application.models.GetCompanyByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetCompanyByIdUseCaseOut;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.core.domain.company.Company;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.application.impl.GetCompanyByIdUseCaseImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCompanyByIdUseCaseTest {

    private final CompanyRepositoryPort repositoryPort = mock(CompanyRepositoryPort.class);
    private final GetCompanyByIdUseCaseImpl service = new GetCompanyByIdUseCaseImpl(repositoryPort);

    @Test
    void shouldGetCompanyByIdSuccessfully() {
        UUID id = UUID.randomUUID();
        GetCompanyByIdUseCaseIn request = new GetCompanyByIdUseCaseIn(id);

        Company company = Company.reconstitute(id, "900123456", "Acme Corp", false, true);
        when(repositoryPort.findById(id)).thenReturn(company);

        GetCompanyByIdUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.companyFound());
        assertEquals(id, result.companyFound().id());
        assertEquals("900123456", result.companyFound().nit());
        assertEquals("Acme Corp", result.companyFound().legalName());
        assertFalse(result.companyFound().payrollTaxExempt());
        assertTrue(result.companyFound().active());
        verify(repositoryPort).findById(id);
    }

    @Test
    void shouldFailWhenIdIsNull() {
        GetCompanyByIdUseCaseIn request = new GetCompanyByIdUseCaseIn(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("company.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldFailWhenCompanyNotFound() {
        UUID id = UUID.randomUUID();
        GetCompanyByIdUseCaseIn request = new GetCompanyByIdUseCaseIn(id);

        when(repositoryPort.findById(id)).thenThrow(new NotFoundException("id", id.toString(), "company.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("company.notFound", ex.getMessage());
        verify(repositoryPort).findById(id);
    }
}
