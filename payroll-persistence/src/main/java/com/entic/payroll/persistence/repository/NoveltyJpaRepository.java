package com.entic.payroll.persistence.repository;

import com.entic.payroll.persistence.entity.Novelty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoveltyJpaRepository extends JpaRepository<Novelty, Long> {
}
