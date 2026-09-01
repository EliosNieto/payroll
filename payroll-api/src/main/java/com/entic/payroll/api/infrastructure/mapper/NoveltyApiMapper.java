package com.entic.payroll.api.infrastructure.mapper;

import com.entic.payroll.api.infrastructure.dto.novelty.CreateNoveltyRequest;
import com.entic.payroll.api.infrastructure.dto.novelty.NoveltyResponse;
import com.entic.payroll.api.infrastructure.dto.novelty.UpdateNoveltyRequest;
import com.entic.payroll.core.application.models.CreateNoveltyUseCaseIn;
import com.entic.payroll.core.application.models.UpdateNoveltyUseCaseIn;
import com.entic.payroll.core.domain.novelty.Novelty;

public final class NoveltyApiMapper {

    private NoveltyApiMapper() {}

    public static CreateNoveltyUseCaseIn toCommand(CreateNoveltyRequest request) {
        return new CreateNoveltyUseCaseIn(
                request.employeeId(),
                request.noveltyType(),
                request.startDate(),
                request.endDate(),
                request.daysApplied()
        );
    }

    public static UpdateNoveltyUseCaseIn toCommand(Long id, UpdateNoveltyRequest request) {
        return new UpdateNoveltyUseCaseIn(
                id,
                request.noveltyType(),
                request.startDate(),
                request.endDate(),
                request.daysApplied()
        );
    }

    public static NoveltyResponse toResponse(Novelty novelty) {
        return new NoveltyResponse(
                novelty.getId(),
                novelty.getEmployeeId(),
                novelty.getNoveltyType(),
                novelty.getStartDate(),
                novelty.getEndDate(),
                novelty.getDaysApplied()
        );
    }
}
