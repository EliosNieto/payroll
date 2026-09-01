package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.GetNoveltyByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetNoveltyByIdUseCaseOut;
import com.entic.payroll.core.application.port.in.GetNoveltyByIdUseCase;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.novelty.Novelty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetNoveltyByIdUseCaseImpl implements GetNoveltyByIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetNoveltyByIdUseCaseImpl.class);

    private final NoveltyRepositoryPort repositoryPort;

    public GetNoveltyByIdUseCaseImpl(NoveltyRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GetNoveltyByIdUseCaseOut execute(GetNoveltyByIdUseCaseIn request) {
        log.info("Getting novelty by id: {}", request.id());

        validateId(request.id());

        Novelty novelty = repositoryPort.findById(request.id());

        log.info("Novelty found with id: {}", novelty.getId());
        return new GetNoveltyByIdUseCaseOut(toNoveltyFound(novelty));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "novelty.id.required");
        }
    }

    private GetNoveltyByIdUseCaseOut.NoveltyFound toNoveltyFound(Novelty novelty) {
        return new GetNoveltyByIdUseCaseOut.NoveltyFound(
                novelty.getId(),
                novelty.getEmployeeId(),
                novelty.getNoveltyType(),
                novelty.getStartDate(),
                novelty.getEndDate(),
                novelty.getDaysApplied()
        );
    }
}
