package com.entic.payroll.api.infrastructure.dto.company;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String nit,
        String legalName,
        boolean payrollTaxExempt,
        boolean active
) {}
