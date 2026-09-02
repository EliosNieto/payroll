package com.entic.payroll.core.application.port.out;

import com.entic.payroll.core.domain.paymentconcept.PaymentConcept;

import java.util.UUID;

public interface PaymentConceptRepositoryPort {

    PaymentConcept save(PaymentConcept paymentConcept);

    PaymentConcept findById(Long id);

    boolean companyExists(UUID companyId);

    boolean existsByCompanyIdAndCode(UUID companyId, String code);

    boolean existsByCompanyIdAndCodeAndIdNot(UUID companyId, String code, Long id);
}
