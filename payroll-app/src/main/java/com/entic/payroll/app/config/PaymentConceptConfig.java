package com.entic.payroll.app.config;

import com.entic.payroll.core.application.impl.CreatePaymentConceptUseCaseImpl;
import com.entic.payroll.core.application.impl.GetPaymentConceptByIdUseCaseImpl;
import com.entic.payroll.core.application.impl.UpdatePaymentConceptUseCaseImpl;
import com.entic.payroll.core.application.port.in.CreatePaymentConceptUseCase;
import com.entic.payroll.core.application.port.in.GetPaymentConceptByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdatePaymentConceptUseCase;
import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.persistence.adapter.out.PaymentConceptPersistenceAdapter;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import com.entic.payroll.persistence.repository.PaymentConceptJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConceptConfig {

    private static final Logger log = LoggerFactory.getLogger(PaymentConceptConfig.class);

    @Bean
    public PaymentConceptRepositoryPort paymentConceptRepositoryPort(
            PaymentConceptJpaRepository repository,
            CompanyJpaRepository companyRepository) {
        log.info("Wiring PaymentConceptPersistenceAdapter as PaymentConceptRepositoryPort");
        return new PaymentConceptPersistenceAdapter(repository, companyRepository);
    }

    @Bean
    public CreatePaymentConceptUseCase createPaymentConceptUseCase(PaymentConceptRepositoryPort port) {
        log.info("Wiring CreatePaymentConceptUseCase");
        return new CreatePaymentConceptUseCaseImpl(port);
    }

    @Bean
    public GetPaymentConceptByIdUseCase getPaymentConceptByIdUseCase(PaymentConceptRepositoryPort port) {
        log.info("Wiring GetPaymentConceptByIdUseCase");
        return new GetPaymentConceptByIdUseCaseImpl(port);
    }

    @Bean
    public UpdatePaymentConceptUseCase updatePaymentConceptUseCase(PaymentConceptRepositoryPort port) {
        log.info("Wiring UpdatePaymentConceptUseCase");
        return new UpdatePaymentConceptUseCaseImpl(port);
    }
}
