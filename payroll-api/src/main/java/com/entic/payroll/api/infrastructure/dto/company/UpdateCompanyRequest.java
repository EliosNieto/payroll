package com.entic.payroll.api.infrastructure.dto.company;

public record UpdateCompanyRequest(
        String nit,
        String legalName,
        boolean payrollTaxExempt
) {}
