package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.UpdatePaymentConceptUseCaseIn;
import com.entic.payroll.core.application.models.UpdatePaymentConceptUseCaseOut;
import com.entic.payroll.core.application.port.in.UpdatePaymentConceptUseCase;
import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.core.domain.enums.ConceptNature;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class UpdatePaymentConceptUseCaseImpl implements UpdatePaymentConceptUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdatePaymentConceptUseCaseImpl.class);

    private final PaymentConceptRepositoryPort repositoryPort;

    public UpdatePaymentConceptUseCaseImpl(PaymentConceptRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public UpdatePaymentConceptUseCaseOut execute(UpdatePaymentConceptUseCaseIn request) {
        log.info("Updating payment concept with id: {}", request.id());

        validateId(request.id());
        validateCode(request.code());
        validateName(request.name());
        validateNature(request.nature());

        PaymentConcept existing = repositoryPort.findById(request.id());
        validateUniqueCode(existing.getCompanyId(), request.code(), request.id());

        PaymentConcept updated = existing.update(request.code(), request.name(), request.nature());
        PaymentConcept saved = repositoryPort.save(updated);

        log.info("Payment concept updated with id: {}", saved.getId());
        return new UpdatePaymentConceptUseCaseOut(toPaymentConceptUpdated(saved));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "paymentConcept.id.required");
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
        if (repositoryPort.existsByCompanyIdAndCodeAndIdNot(companyId, code, excludingId)) {
            throw new AlreadyExistsException("code", code, "paymentConcept.code.alreadyExists");
        }
    }

    private UpdatePaymentConceptUseCaseOut.PaymentConceptUpdated toPaymentConceptUpdated(
            PaymentConcept paymentConcept) {
        return new UpdatePaymentConceptUseCaseOut.PaymentConceptUpdated(
                paymentConcept.getId(),
                paymentConcept.getCompanyId(),
                paymentConcept.getCode(),
                paymentConcept.getName(),
                paymentConcept.getNature()
        );
    }
}
