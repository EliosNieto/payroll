package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.CreateNoveltyUseCaseIn;
import com.entic.payroll.core.application.models.CreateNoveltyUseCaseOut;
import com.entic.payroll.core.application.port.in.CreateNoveltyUseCase;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.enums.NoveltyType;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.novelty.Novelty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class CreateNoveltyUseCaseImpl implements CreateNoveltyUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateNoveltyUseCaseImpl.class);

    private final NoveltyRepositoryPort repositoryPort;

    public CreateNoveltyUseCaseImpl(NoveltyRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CreateNoveltyUseCaseOut execute(CreateNoveltyUseCaseIn request) {
        log.info("Creating novelty for employee: {} of type: {}", request.employeeId(), request.noveltyType());

        validateEmployeeId(request.employeeId());
        validateEmployeeExists(request.employeeId());
        validateNoveltyType(request.noveltyType());
        validateStartDate(request.startDate());
        validateDateRange(request.startDate(), request.endDate());
        validateDaysApplied(request.daysApplied());

        Novelty novelty = Novelty.create(request.employeeId(), request.noveltyType(),
                request.startDate(), request.endDate(), request.daysApplied());
        Novelty saved = repositoryPort.save(novelty);

        log.info("Novelty created with id: {}", saved.getId());
        return new CreateNoveltyUseCaseOut(toNoveltyCreated(saved));
    }

    private void validateEmployeeId(Long employeeId) {
        if (employeeId == null) {
            throw new ValidationException("employeeId", null, "novelty.employeeId.required");
        }
    }

    private void validateEmployeeExists(Long employeeId) {
        if (!repositoryPort.employeeExists(employeeId)) {
            throw new NotFoundException("employeeId", employeeId.toString(), "novelty.employee.notFound");
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

    private CreateNoveltyUseCaseOut.NoveltyCreated toNoveltyCreated(Novelty novelty) {
        return new CreateNoveltyUseCaseOut.NoveltyCreated(
                novelty.getId(),
                novelty.getEmployeeId(),
                novelty.getNoveltyType(),
                novelty.getStartDate(),
                novelty.getEndDate(),
                novelty.getDaysApplied()
        );
    }
}
