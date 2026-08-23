package com.entic.payroll.app.config;

import com.entic.payroll.core.application.impl.CreateCompanyUseCaseImpl;
import com.entic.payroll.core.application.impl.GetCompanyByIdUseCaseImpl;
import com.entic.payroll.core.application.impl.UpdateCompanyUseCaseImpl;
import com.entic.payroll.core.application.port.in.CreateCompanyUseCase;
import com.entic.payroll.core.application.port.in.GetCompanyByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateCompanyUseCase;
import com.entic.payroll.core.application.port.out.CompanyRepositoryPort;
import com.entic.payroll.persistence.adapter.out.CompanyPersistenceAdapter;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompanyConfig {

    private static final Logger log = LoggerFactory.getLogger(CompanyConfig.class);

    @Bean
    public CompanyRepositoryPort companyRepositoryPort(CompanyJpaRepository repository) {
        log.info("Wiring CompanyPersistenceAdapter as CompanyRepositoryPort");
        return new CompanyPersistenceAdapter(repository);
    }

    @Bean
    public CreateCompanyUseCase createCompanyService(CompanyRepositoryPort port) {
        log.info("Wiring CreateCompanyService");
        return new CreateCompanyUseCaseImpl(port);
    }

    @Bean
    public GetCompanyByIdUseCase getCompanyByIdUseCase(CompanyRepositoryPort port) {
        log.info("Wiring GetCompanyByIdUseCase");
        return new GetCompanyByIdUseCaseImpl(port);
    }

    @Bean
    public UpdateCompanyUseCase updateCompanyUseCase(CompanyRepositoryPort port) {
        log.info("Wiring UpdateCompanyUseCase");
        return new UpdateCompanyUseCaseImpl(port);
    }
}
