package com.entic.payroll.app.config;

import com.entic.payroll.core.application.company.CreateCompanyService;
import com.entic.payroll.core.application.company.port.out.CompanyRepositoryPort;
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
    public CreateCompanyService createCompanyService(CompanyRepositoryPort port) {
        log.info("Wiring CreateCompanyService");
        return new CreateCompanyService(port);
    }
}
