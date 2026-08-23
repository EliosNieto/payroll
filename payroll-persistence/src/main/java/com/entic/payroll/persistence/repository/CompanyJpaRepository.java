package com.entic.payroll.persistence.repository;

import com.entic.payroll.persistence.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyJpaRepository extends JpaRepository<Company, UUID> {

    boolean existsByNit(String nit);

    boolean existsByNitAndIdNot(String nit, UUID id);
}
