package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;

public record CreateCompanyUseCaseIn (
        String nit,
        String legalName,
        boolean payrollTaxExempt
) implements ApplicationRequest {
}
