package com.entic.payroll.persistence.repository;

import com.entic.payroll.persistence.entity.PaymentConcept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentConceptJpaRepository extends JpaRepository<PaymentConcept, Long> {

    boolean existsByCompanyIdAndCode(UUID companyId, String code);

    boolean existsByCompanyIdAndCodeAndIdNot(UUID companyId, String code, Long id);
}
