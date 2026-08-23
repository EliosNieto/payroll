package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;

import java.util.UUID;

public record UpdateCompanyUseCaseIn(
        UUID id,
        String nit,
        String legalName,
        boolean payrollTaxExempt
) implements ApplicationRequest {
}
