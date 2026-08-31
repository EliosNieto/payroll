package com.entic.payroll.app.config;

import com.entic.payroll.core.application.impl.CreateEmployeeUseCaseImpl;
import com.entic.payroll.core.application.impl.GetEmployeeByIdUseCaseImpl;
import com.entic.payroll.core.application.impl.UpdateEmployeeUseCaseImpl;
import com.entic.payroll.core.application.port.in.CreateEmployeeUseCase;
import com.entic.payroll.core.application.port.in.GetEmployeeByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateEmployeeUseCase;
import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.persistence.adapter.out.EmployeePersistenceAdapter;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import com.entic.payroll.persistence.repository.EmployeeJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeConfig {

    private static final Logger log = LoggerFactory.getLogger(EmployeeConfig.class);

    @Bean
    public EmployeeRepositoryPort employeeRepositoryPort(EmployeeJpaRepository repository,
                                                         CompanyJpaRepository companyRepository) {
        log.info("Wiring EmployeePersistenceAdapter as EmployeeRepositoryPort");
        return new EmployeePersistenceAdapter(repository, companyRepository);
    }

    @Bean
    public CreateEmployeeUseCase createEmployeeUseCase(EmployeeRepositoryPort port) {
        log.info("Wiring CreateEmployeeUseCase");
        return new CreateEmployeeUseCaseImpl(port);
    }

    @Bean
    public GetEmployeeByIdUseCase getEmployeeByIdUseCase(EmployeeRepositoryPort port) {
        log.info("Wiring GetEmployeeByIdUseCase");
        return new GetEmployeeByIdUseCaseImpl(port);
    }

    @Bean
    public UpdateEmployeeUseCase updateEmployeeUseCase(EmployeeRepositoryPort port) {
        log.info("Wiring UpdateEmployeeUseCase");
        return new UpdateEmployeeUseCaseImpl(port);
    }
}