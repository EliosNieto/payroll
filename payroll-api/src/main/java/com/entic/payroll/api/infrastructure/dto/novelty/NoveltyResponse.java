package com.entic.payroll.api.infrastructure.dto.novelty;

import com.entic.payroll.core.domain.enums.NoveltyType;

import java.time.LocalDate;

public record NoveltyResponse(
        Long id,
        Long employeeId,
        NoveltyType noveltyType,
        LocalDate startDate,
        LocalDate endDate,
        int daysApplied
) {}
