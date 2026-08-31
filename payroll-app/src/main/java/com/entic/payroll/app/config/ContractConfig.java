package com.entic.payroll.app.config;

import com.entic.payroll.core.application.impl.CreateContractUseCaseImpl;
import com.entic.payroll.core.application.impl.GetContractByIdUseCaseImpl;
import com.entic.payroll.core.application.impl.UpdateContractUseCaseImpl;
import com.entic.payroll.core.application.port.in.CreateContractUseCase;
import com.entic.payroll.core.application.port.in.GetContractByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateContractUseCase;
import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.persistence.adapter.out.ContractPersistenceAdapter;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import com.entic.payroll.persistence.repository.ContractJpaRepository;
import com.entic.payroll.persistence.repository.EmployeeJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractConfig {

    private static final Logger log = LoggerFactory.getLogger(ContractConfig.class);

    @Bean
    public ContractRepositoryPort contractRepositoryPort(ContractJpaRepository repository,
                                                         CompanyJpaRepository companyRepository,
                                                         EmployeeJpaRepository employeeRepository) {
        log.info("Wiring ContractPersistenceAdapter as ContractRepositoryPort");
        return new ContractPersistenceAdapter(repository, companyRepository, employeeRepository);
    }

    @Bean
    public CreateContractUseCase createContractUseCase(ContractRepositoryPort port) {
        log.info("Wiring CreateContractUseCase");
        return new CreateContractUseCaseImpl(port);
    }

    @Bean
    public GetContractByIdUseCase getContractByIdUseCase(ContractRepositoryPort port) {
        log.info("Wiring GetContractByIdUseCase");
        return new GetContractByIdUseCaseImpl(port);
    }

    @Bean
    public UpdateContractUseCase updateContractUseCase(ContractRepositoryPort port) {
        log.info("Wiring UpdateContractUseCase");
        return new UpdateContractUseCaseImpl(port);
    }
}
