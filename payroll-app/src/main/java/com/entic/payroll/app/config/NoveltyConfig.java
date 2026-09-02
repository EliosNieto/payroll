package com.entic.payroll.app.config;

import com.entic.payroll.core.application.impl.CreateNoveltyUseCaseImpl;
import com.entic.payroll.core.application.impl.GetNoveltyByIdUseCaseImpl;
import com.entic.payroll.core.application.impl.UpdateNoveltyUseCaseImpl;
import com.entic.payroll.core.application.port.in.CreateNoveltyUseCase;
import com.entic.payroll.core.application.port.in.GetNoveltyByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateNoveltyUseCase;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.persistence.adapter.out.NoveltyPersistenceAdapter;
import com.entic.payroll.persistence.repository.EmployeeJpaRepository;
import com.entic.payroll.persistence.repository.NoveltyJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoveltyConfig {

    private static final Logger log = LoggerFactory.getLogger(NoveltyConfig.class);

    @Bean
    public NoveltyRepositoryPort noveltyRepositoryPort(NoveltyJpaRepository repository,
                                                       EmployeeJpaRepository employeeRepository) {
        log.info("Wiring NoveltyPersistenceAdapter as NoveltyRepositoryPort");
        return new NoveltyPersistenceAdapter(repository, employeeRepository);
    }

    @Bean
    public CreateNoveltyUseCase createNoveltyUseCase(NoveltyRepositoryPort port) {
        log.info("Wiring CreateNoveltyUseCase");
        return new CreateNoveltyUseCaseImpl(port);
    }

    @Bean
    public GetNoveltyByIdUseCase getNoveltyByIdUseCase(NoveltyRepositoryPort port) {
        log.info("Wiring GetNoveltyByIdUseCase");
        return new GetNoveltyByIdUseCaseImpl(port);
    }

    @Bean
    public UpdateNoveltyUseCase updateNoveltyUseCase(NoveltyRepositoryPort port) {
        log.info("Wiring UpdateNoveltyUseCase");
        return new UpdateNoveltyUseCaseImpl(port);
    }
}
