package com.entic.payroll.core.application.port.out;

import com.entic.payroll.core.domain.novelty.Novelty;

public interface NoveltyRepositoryPort {

    Novelty save(Novelty novelty);

    Novelty findById(Long id);

    boolean employeeExists(Long employeeId);
}
