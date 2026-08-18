package com.entic.payroll.api.infrastructure.dto.company;

public record CreateCompanyRequest(
        String nit,
        String legalName,
        boolean payrollTaxExempt
) {}
