package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationResponse;
import com.entic.payroll.core.domain.enums.NoveltyType;

import java.time.LocalDate;

public record GetNoveltyByIdUseCaseOut(
        NoveltyFound noveltyFound
) implements ApplicationResponse {

    public record NoveltyFound(
            Long id,
            Long employeeId,
            NoveltyType noveltyType,
            LocalDate startDate,
            LocalDate endDate,
            int daysApplied
    ) {}
}
