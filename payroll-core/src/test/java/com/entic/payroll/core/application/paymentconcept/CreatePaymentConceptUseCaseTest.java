package com.entic.payroll.core.application.paymentconcept;

import com.entic.payroll.core.application.impl.CreatePaymentConceptUseCaseImpl;
import com.entic.payroll.core.application.models.CreatePaymentConceptUseCaseIn;
import com.entic.payroll.core.application.models.CreatePaymentConceptUseCaseOut;
import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.core.domain.enums.ConceptNature;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePaymentConceptUseCaseTest {

    private final PaymentConceptRepositoryPort repositoryPort = mock(PaymentConceptRepositoryPort.class);
    private final CreatePaymentConceptUseCaseImpl service = new CreatePaymentConceptUseCaseImpl(repositoryPort);

    private static final UUID COMPANY_ID = UUID.randomUUID();
    private static final String CODE = "SUELDO";
    private static final String NAME = "Sueldo";
    private static final ConceptNature NATURE = ConceptNature.EARNINGS;

    private CreatePaymentConceptUseCaseIn validCommand() {
        return new CreatePaymentConceptUseCaseIn(COMPANY_ID, CODE, NAME, NATURE);
    }

    @Test
    void shouldCreatePaymentConceptSuccessfully() {
        CreatePaymentConceptUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.existsByCompanyIdAndCode(COMPANY_ID, CODE)).thenReturn(false);
        when(repositoryPort.save(any(PaymentConcept.class))).thenAnswer(invocation -> {
            PaymentConcept c = invocation.getArgument(0);
            return PaymentConcept.reconstitute(1L, c.getCompanyId(), c.getCode(), c.getName(), c.getNature());
        });

        CreatePaymentConceptUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNotNull(result.paymentConceptCreated());
        assertNotNull(result.paymentConceptCreated().id());
        assertEquals(COMPANY_ID, result.paymentConceptCreated().companyId());
        assertEquals(CODE, result.paymentConceptCreated().code());
        assertEquals(NAME, result.paymentConceptCreated().name());
        assertEquals(NATURE, result.paymentConceptCreated().nature());
        verify(repositoryPort).save(any(PaymentConcept.class));
    }

    @Test
    void shouldCreateGlobalPaymentConceptWithoutCompany() {
        CreatePaymentConceptUseCaseIn command = new CreatePaymentConceptUseCaseIn(null, CODE, NAME, NATURE);

        when(repositoryPort.existsByCompanyIdAndCode(null, CODE)).thenReturn(false);
        when(repositoryPort.save(any(PaymentConcept.class))).thenAnswer(invocation -> {
            PaymentConcept c = invocation.getArgument(0);
            return PaymentConcept.reconstitute(2L, c.getCompanyId(), c.getCode(), c.getName(), c.getNature());
        });

        CreatePaymentConceptUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNull(result.paymentConceptCreated().companyId());
        verify(repositoryPort).save(any(PaymentConcept.class));
        verify(repositoryPort, never()).companyExists(any());
    }

    @Test
    void shouldFailWhenCompanyNotFound() {
        CreatePaymentConceptUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(command));
        assertEquals("paymentConcept.company.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenCodeIsBlank() {
        CreatePaymentConceptUseCaseIn command = new CreatePaymentConceptUseCaseIn(COMPANY_ID, "  ", NAME, NATURE);
        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("paymentConcept.code.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        CreatePaymentConceptUseCaseIn command = new CreatePaymentConceptUseCaseIn(COMPANY_ID, CODE, null, NATURE);
        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("paymentConcept.name.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNatureIsNull() {
        CreatePaymentConceptUseCaseIn command = new CreatePaymentConceptUseCaseIn(COMPANY_ID, CODE, NAME, null);
        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("paymentConcept.nature.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenCodeAlreadyExists() {
        CreatePaymentConceptUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.existsByCompanyIdAndCode(COMPANY_ID, CODE)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(command));
        assertEquals("paymentConcept.code.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
