package com.entic.payroll.persistence.mapper;

import com.entic.payroll.core.domain.novelty.Novelty;
import com.entic.payroll.persistence.entity.Employee;

public final class NoveltyMapper {

    private NoveltyMapper() {}

    public static Novelty toDomain(com.entic.payroll.persistence.entity.Novelty entity) {
        return Novelty.reconstitute(
                entity.getId(),
                entity.getEmployee().getId(),
                entity.getNoveltyType(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDaysApplied()
        );
    }

    public static com.entic.payroll.persistence.entity.Novelty toEntity(
            Novelty domain,
            Employee employee) {
        com.entic.payroll.persistence.entity.Novelty entity =
                new com.entic.payroll.persistence.entity.Novelty();
        entity.setId(domain.getId());
        entity.setEmployee(employee);
        entity.setNoveltyType(domain.getNoveltyType());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setDaysApplied(domain.getDaysApplied());
        return entity;
    }
}
