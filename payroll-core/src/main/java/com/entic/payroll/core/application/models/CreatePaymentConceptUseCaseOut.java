package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;
import com.entic.payroll.core.domain.enums.ConceptNature;

import java.util.UUID;

public record CreatePaymentConceptUseCaseOut(
        PaymentConceptCreated paymentConceptCreated
) implements ApplicationResponse {

    public record PaymentConceptCreated(
            Long id,
            UUID companyId,
            String code,
            String name,
            ConceptNature nature
    ) {}
}
