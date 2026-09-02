package com.entic.payroll.core.application.paymentconcept;

import com.entic.payroll.core.application.impl.UpdatePaymentConceptUseCaseImpl;
import com.entic.payroll.core.application.models.UpdatePaymentConceptUseCaseIn;
import com.entic.payroll.core.application.models.UpdatePaymentConceptUseCaseOut;
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

class UpdatePaymentConceptUseCaseTest {

    private final PaymentConceptRepositoryPort repositoryPort = mock(PaymentConceptRepositoryPort.class);
    private final UpdatePaymentConceptUseCaseImpl service = new UpdatePaymentConceptUseCaseImpl(repositoryPort);

    private static final Long ID = 5L;
    private static final UUID COMPANY_ID = UUID.randomUUID();

    private UpdatePaymentConceptUseCaseIn validCommand() {
        return new UpdatePaymentConceptUseCaseIn(ID, "BONO", "Bono", ConceptNature.EARNINGS);
    }

    @Test
    void shouldUpdatePaymentConceptSuccessfully() {
        UpdatePaymentConceptUseCaseIn request = validCommand();

        PaymentConcept existing = PaymentConcept.reconstitute(ID, COMPANY_ID, "SUELDO", "Sueldo",
                ConceptNature.EARNINGS);
        when(repositoryPort.findById(ID)).thenReturn(existing);
        when(repositoryPort.existsByCompanyIdAndCodeAndIdNot(COMPANY_ID, "BONO", ID)).thenReturn(false);
        when(repositoryPort.save(any(PaymentConcept.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdatePaymentConceptUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.paymentConceptUpdated());
        assertEquals(ID, result.paymentConceptUpdated().id());
        assertEquals(COMPANY_ID, result.paymentConceptUpdated().companyId());
        assertEquals("BONO", result.paymentConceptUpdated().code());
        assertEquals("Bono", result.paymentConceptUpdated().name());
        assertEquals(ConceptNature.EARNINGS, result.paymentConceptUpdated().nature());
        verify(repositoryPort).save(any(PaymentConcept.class));
    }

    @Test
    void shouldFailWhenIdIsNull() {
        UpdatePaymentConceptUseCaseIn request = new UpdatePaymentConceptUseCaseIn(null, "BONO", "Bono",
                ConceptNature.EARNINGS);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("paymentConcept.id.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNotFound() {
        UpdatePaymentConceptUseCaseIn request = validCommand();

        when(repositoryPort.findById(ID)).thenThrow(new NotFoundException("id", ID.toString(), "paymentConcept.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("paymentConcept.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenCodeIsBlank() {
        UpdatePaymentConceptUseCaseIn request = new UpdatePaymentConceptUseCaseIn(ID, null, "Bono",
                ConceptNature.EARNINGS);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("paymentConcept.code.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        UpdatePaymentConceptUseCaseIn request = new UpdatePaymentConceptUseCaseIn(ID, "BONO", "",
                ConceptNature.EARNINGS);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("paymentConcept.name.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNatureIsNull() {
        UpdatePaymentConceptUseCaseIn request = new UpdatePaymentConceptUseCaseIn(ID, "BONO", "Bono", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("paymentConcept.nature.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenCodeAlreadyExists() {
        UpdatePaymentConceptUseCaseIn request = validCommand();

        PaymentConcept existing = PaymentConcept.reconstitute(ID, COMPANY_ID, "SUELDO", "Sueldo",
                ConceptNature.EARNINGS);
        when(repositoryPort.findById(ID)).thenReturn(existing);
        when(repositoryPort.existsByCompanyIdAndCodeAndIdNot(COMPANY_ID, "BONO", ID)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(request));
        assertEquals("paymentConcept.code.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
