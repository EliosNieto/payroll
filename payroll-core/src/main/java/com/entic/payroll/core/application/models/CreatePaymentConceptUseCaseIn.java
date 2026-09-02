package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;
import com.entic.payroll.core.domain.enums.ConceptNature;

import java.util.UUID;

public record CreatePaymentConceptUseCaseIn(
        UUID companyId,
        String code,
        String name,
        ConceptNature nature
) implements ApplicationRequest {
}
