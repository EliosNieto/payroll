package com.entic.payroll.api.infrastructure.dto.paymentconcept;

import com.entic.payroll.core.domain.enums.ConceptNature;

import java.util.UUID;

public record PaymentConceptResponse(
        Long id,
        UUID companyId,
        String code,
        String name,
        ConceptNature nature
) {}
