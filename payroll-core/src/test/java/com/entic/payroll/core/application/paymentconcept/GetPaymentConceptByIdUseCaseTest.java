package com.entic.payroll.core.application.paymentconcept;

import com.entic.payroll.core.application.impl.GetPaymentConceptByIdUseCaseImpl;
import com.entic.payroll.core.application.models.GetPaymentConceptByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetPaymentConceptByIdUseCaseOut;
import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.core.domain.enums.ConceptNature;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentConceptByIdUseCaseTest {

    private final PaymentConceptRepositoryPort repositoryPort = mock(PaymentConceptRepositoryPort.class);
    private final GetPaymentConceptByIdUseCaseImpl service = new GetPaymentConceptByIdUseCaseImpl(repositoryPort);

    @Test
    void shouldGetPaymentConceptByIdSuccessfully() {
        Long id = 5L;
        UUID companyId = UUID.randomUUID();
        GetPaymentConceptByIdUseCaseIn request = new GetPaymentConceptByIdUseCaseIn(id);

        PaymentConcept paymentConcept = PaymentConcept.reconstitute(id, companyId, "SUELDO", "Sueldo",
                ConceptNature.EARNINGS);
        when(repositoryPort.findById(id)).thenReturn(paymentConcept);

        GetPaymentConceptByIdUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.paymentConceptFound());
        assertEquals(id, result.paymentConceptFound().id());
        assertEquals(companyId, result.paymentConceptFound().companyId());
        assertEquals("SUELDO", result.paymentConceptFound().code());
        assertEquals("Sueldo", result.paymentConceptFound().name());
        assertEquals(ConceptNature.EARNINGS, result.paymentConceptFound().nature());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        GetPaymentConceptByIdUseCaseIn request = new GetPaymentConceptByIdUseCaseIn(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("paymentConcept.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }

    @Test
    void shouldThrowWhenNotFound() {
        Long id = 99L;
        GetPaymentConceptByIdUseCaseIn request = new GetPaymentConceptByIdUseCaseIn(id);

        when(repositoryPort.findById(id)).thenThrow(new NotFoundException("id", id.toString(), "paymentConcept.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("paymentConcept.notFound", ex.getMessage());
    }
}
