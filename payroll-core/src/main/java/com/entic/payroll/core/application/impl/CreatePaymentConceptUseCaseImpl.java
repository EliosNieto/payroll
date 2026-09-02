package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.CreatePaymentConceptUseCaseIn;
import com.entic.payroll.core.application.models.CreatePaymentConceptUseCaseOut;
import com.entic.payroll.core.application.port.in.CreatePaymentConceptUseCase;
import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.core.domain.enums.ConceptNature;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CreatePaymentConceptUseCaseImpl implements CreatePaymentConceptUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreatePaymentConceptUseCaseImpl.class);

    private final PaymentConceptRepositoryPort repositoryPort;

    public CreatePaymentConceptUseCaseImpl(PaymentConceptRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CreatePaymentConceptUseCaseOut execute(CreatePaymentConceptUseCaseIn request) {
        log.info("Creating payment concept in company: {}", request.companyId());

        validateCompany(request.companyId());
        validateCode(request.code());
        validateName(request.name());
        validateNature(request.nature());
        validateUniqueCode(request.companyId(), request.code(), null);

        PaymentConcept paymentConcept = PaymentConcept.create(
                request.companyId(), request.code(), request.name(), request.nature());
        PaymentConcept saved = repositoryPort.save(paymentConcept);

        log.info("Payment concept created with id: {}", saved.getId());
        return new CreatePaymentConceptUseCaseOut(toPaymentConceptCreated(saved));
    }

    private void validateCompany(UUID companyId) {
        if (companyId != null && !repositoryPort.companyExists(companyId)) {
            throw new NotFoundException("companyId", companyId.toString(), "paymentConcept.company.notFound");
        }
    }

    private void validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("code", code, "paymentConcept.code.required");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", name, "paymentConcept.name.required");
        }
    }

    private void validateNature(ConceptNature nature) {
        if (nature == null) {
            throw new ValidationException("nature", null, "paymentConcept.nature.required");
        }
    }

    private void validateUniqueCode(UUID companyId, String code, Long excludingId) {
        boolean exists;
        if (excludingId == null) {
            exists = repositoryPort.existsByCompanyIdAndCode(companyId, code);
        } else {
            exists = repositoryPort.existsByCompanyIdAndCodeAndIdNot(companyId, code, excludingId);
        }
        if (exists) {
            throw new AlreadyExistsException("code", code, "paymentConcept.code.alreadyExists");
        }
    }

    private CreatePaymentConceptUseCaseOut.PaymentConceptCreated toPaymentConceptCreated(
            PaymentConcept paymentConcept) {
        return new CreatePaymentConceptUseCaseOut.PaymentConceptCreated(
                paymentConcept.getId(),
                paymentConcept.getCompanyId(),
                paymentConcept.getCode(),
                paymentConcept.getName(),
                paymentConcept.getNature()
        );
    }
}
