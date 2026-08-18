package com.entic.payroll.core.application.company;

public record CreateCompanyCommand(
        String nit,
        String legalName,
        boolean payrollTaxExempt
) {}
