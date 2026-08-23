package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;

import java.util.UUID;

public record GetCompanyByIdUseCaseIn(
        UUID id
) implements ApplicationRequest {
}
