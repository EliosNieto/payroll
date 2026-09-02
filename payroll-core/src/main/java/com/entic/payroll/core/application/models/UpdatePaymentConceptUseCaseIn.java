package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;
import com.entic.payroll.core.domain.enums.ConceptNature;

public record UpdatePaymentConceptUseCaseIn(
        Long id,
        String code,
        String name,
        ConceptNature nature
) implements ApplicationRequest {
}
