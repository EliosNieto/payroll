package com.entic.payroll.api.infrastructure.mapper;

import com.entic.payroll.api.infrastructure.dto.paymentconcept.CreatePaymentConceptRequest;
import com.entic.payroll.api.infrastructure.dto.paymentconcept.UpdatePaymentConceptRequest;
import com.entic.payroll.core.application.models.CreatePaymentConceptUseCaseIn;
import com.entic.payroll.core.application.models.UpdatePaymentConceptUseCaseIn;

public final class PaymentConceptApiMapper {

    private PaymentConceptApiMapper() {}

    public static CreatePaymentConceptUseCaseIn toCommand(CreatePaymentConceptRequest request) {
        return new CreatePaymentConceptUseCaseIn(
                request.companyId(),
                request.code(),
                request.name(),
                request.nature()
        );
    }

    public static UpdatePaymentConceptUseCaseIn toCommand(Long id, UpdatePaymentConceptRequest request) {
        return new UpdatePaymentConceptUseCaseIn(
                id,
                request.code(),
                request.name(),
                request.nature()
        );
    }
}
