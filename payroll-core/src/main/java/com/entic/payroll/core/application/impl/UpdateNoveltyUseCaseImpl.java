package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.UpdateNoveltyUseCaseIn;
import com.entic.payroll.core.application.models.UpdateNoveltyUseCaseOut;
import com.entic.payroll.core.application.port.in.UpdateNoveltyUseCase;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.enums.NoveltyType;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.novelty.Novelty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class UpdateNoveltyUseCaseImpl implements UpdateNoveltyUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateNoveltyUseCaseImpl.class);

    private final NoveltyRepositoryPort repositoryPort;

    public UpdateNoveltyUseCaseImpl(NoveltyRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UpdateNoveltyUseCaseOut execute(UpdateNoveltyUseCaseIn request) {
        log.info("Updating novelty with id: {}", request.id());

        validateId(request.id());
        validateNoveltyType(request.noveltyType());
        validateStartDate(request.startDate());
        validateDateRange(request.startDate(), request.endDate());
        validateDaysApplied(request.daysApplied());

        Novelty existing = repositoryPort.findById(request.id());

        Novelty updated = existing.update(request.noveltyType(), request.startDate(),
                request.endDate(), request.daysApplied());
        Novelty saved = repositoryPort.save(updated);

        log.info("Novelty updated with id: {}", saved.getId());
        return new UpdateNoveltyUseCaseOut(toNoveltyUpdated(saved));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "novelty.id.required");
        }
    }

    private void validateNoveltyType(NoveltyType noveltyType) {
        if (noveltyType == null) {
            throw new ValidationException("noveltyType", null, "novelty.noveltyType.required");
        }
    }

    private void validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new ValidationException("startDate", null, "novelty.startDate.required");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new ValidationException("endDate", endDate.toString(), "novelty.dateRange.invalid");
        }
    }

    private void validateDaysApplied(int daysApplied) {
        if (daysApplied <= 0) {
            throw new ValidationException("daysApplied", String.valueOf(daysApplied), "novelty.daysApplied.invalid");
        }
    }

    private UpdateNoveltyUseCaseOut.NoveltyUpdated toNoveltyUpdated(Novelty novelty) {
        return new UpdateNoveltyUseCaseOut.NoveltyUpdated(
                novelty.getId(),
                novelty.getEmployeeId(),
                novelty.getNoveltyType(),
                novelty.getStartDate(),
                novelty.getEndDate(),
                novelty.getDaysApplied()
        );
    }
}
