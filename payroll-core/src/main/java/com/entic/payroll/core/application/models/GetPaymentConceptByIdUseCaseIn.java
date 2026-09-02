package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;

public record GetPaymentConceptByIdUseCaseIn(
        Long id
) implements ApplicationRequest {
}
