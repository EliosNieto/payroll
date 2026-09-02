package com.entic.payroll.persistence.adapter.out;

import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import com.entic.payroll.persistence.entity.Company;
import com.entic.payroll.persistence.mapper.PaymentConceptMapper;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import com.entic.payroll.persistence.repository.PaymentConceptJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PaymentConceptPersistenceAdapter implements PaymentConceptRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(PaymentConceptPersistenceAdapter.class);

    private final PaymentConceptJpaRepository repository;
    private final CompanyJpaRepository companyRepository;

    public PaymentConceptPersistenceAdapter(PaymentConceptJpaRepository repository,
                                            CompanyJpaRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaymentConcept save(PaymentConcept paymentConcept) {
        log.debug("Saving payment concept with code: {} in company: {}",
                paymentConcept.getCode(), paymentConcept.getCompanyId());
        Company company = null;
        if (paymentConcept.getCompanyId() != null) {
            company = companyRepository.findById(paymentConcept.getCompanyId())
                    .orElseThrow(() -> new NotFoundException("companyId",
                            paymentConcept.getCompanyId().toString(), "paymentConcept.company.notFound"));
        }
        com.entic.payroll.persistence.entity.PaymentConcept entity =
                PaymentConceptMapper.toEntity(paymentConcept, company);
        com.entic.payroll.persistence.entity.PaymentConcept saved = repository.save(entity);
        return PaymentConceptMapper.toDomain(saved);
    }

    @Override
    public PaymentConcept findById(Long id) {
        log.debug("Finding payment concept by id: {}", id);
        com.entic.payroll.persistence.entity.PaymentConcept entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", id.toString(), "paymentConcept.notFound"));
        return PaymentConceptMapper.toDomain(entity);
    }

    @Override
    public boolean companyExists(UUID companyId) {
        log.debug("Checking if company exists with id: {}", companyId);
        return companyRepository.existsById(companyId);
    }

    @Override
    public boolean existsByCompanyIdAndCode(UUID companyId, String code) {
        log.debug("Checking if payment concept exists with code: {} in company: {}", code, companyId);
        return repository.existsByCompanyIdAndCode(companyId, code);
    }

    @Override
    public boolean existsByCompanyIdAndCodeAndIdNot(UUID companyId, String code, Long id) {
        log.debug("Checking if payment concept exists with code: {} in company: {} excluding id: {}",
                code, companyId, id);
        return repository.existsByCompanyIdAndCodeAndIdNot(companyId, code, id);
    }
}
