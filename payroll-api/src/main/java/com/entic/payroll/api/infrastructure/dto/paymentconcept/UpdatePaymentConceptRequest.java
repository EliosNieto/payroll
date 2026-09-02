package com.entic.payroll.api.infrastructure.dto.paymentconcept;

import com.entic.payroll.core.domain.enums.ConceptNature;

public record UpdatePaymentConceptRequest(
        String code,
        String name,
        ConceptNature nature
) {}
