package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;
import com.entic.payroll.core.domain.enums.ConceptNature;

import java.util.UUID;

public record UpdatePaymentConceptUseCaseOut(
        PaymentConceptUpdated paymentConceptUpdated
) implements ApplicationResponse {

    public record PaymentConceptUpdated(
            Long id,
            UUID companyId,
            String code,
            String name,
            ConceptNature nature
    ) {}
}
