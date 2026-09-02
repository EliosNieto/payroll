package com.entic.payroll.core.application.impl;

import com.entic.payroll.core.application.models.GetPaymentConceptByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetPaymentConceptByIdUseCaseOut;
import com.entic.payroll.core.application.port.in.GetPaymentConceptByIdUseCase;
import com.entic.payroll.core.application.port.out.PaymentConceptRepositoryPort;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetPaymentConceptByIdUseCaseImpl implements GetPaymentConceptByIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetPaymentConceptByIdUseCaseImpl.class);

    private final PaymentConceptRepositoryPort repositoryPort;

    public GetPaymentConceptByIdUseCaseImpl(PaymentConceptRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GetPaymentConceptByIdUseCaseOut execute(GetPaymentConceptByIdUseCaseIn request) {
        log.info("Getting payment concept by id: {}", request.id());

        validateId(request.id());

        PaymentConcept paymentConcept = repositoryPort.findById(request.id());

        log.info("Payment concept found with id: {}", paymentConcept.getId());
        return new GetPaymentConceptByIdUseCaseOut(toPaymentConceptFound(paymentConcept));
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new ValidationException("id", null, "paymentConcept.id.required");
        }
    }

    private GetPaymentConceptByIdUseCaseOut.PaymentConceptFound toPaymentConceptFound(
            PaymentConcept paymentConcept) {
        return new GetPaymentConceptByIdUseCaseOut.PaymentConceptFound(
                paymentConcept.getId(),
                paymentConcept.getCompanyId(),
                paymentConcept.getCode(),
                paymentConcept.getName(),
                paymentConcept.getNature()
        );
    }
}
