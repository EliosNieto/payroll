package com.entic.payroll.core.application.models;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;
import com.entic.payroll.core.domain.enums.NoveltyType;

import java.time.LocalDate;

public record CreateNoveltyUseCaseIn(
        Long employeeId,
        NoveltyType noveltyType,
        LocalDate startDate,
        LocalDate endDate,
        int daysApplied
) implements ApplicationRequest {
}
