package com.entic.payroll.core.domain.novelty;

import com.entic.payroll.core.domain.enums.NoveltyType;

import java.time.LocalDate;
import java.util.Objects;

public class Novelty {

    private final Long id;
    private final Long employeeId;
    private final NoveltyType noveltyType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int daysApplied;

    private Novelty(Long id, Long employeeId, NoveltyType noveltyType,
                    LocalDate startDate, LocalDate endDate, int daysApplied) {
        this.id = id;
        this.employeeId = employeeId;
        this.noveltyType = noveltyType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.daysApplied = daysApplied;
    }

    public static Novelty create(Long employeeId, NoveltyType noveltyType,
                                 LocalDate startDate, LocalDate endDate, int daysApplied) {
        return new Novelty(null, employeeId, noveltyType, startDate, endDate, daysApplied);
    }

    public static Novelty reconstitute(Long id, Long employeeId, NoveltyType noveltyType,
                                       LocalDate startDate, LocalDate endDate, int daysApplied) {
        return new Novelty(id, employeeId, noveltyType, startDate, endDate, daysApplied);
    }

    public Novelty update(NoveltyType noveltyType, LocalDate startDate, LocalDate endDate, int daysApplied) {
        return new Novelty(this.id, this.employeeId, noveltyType, startDate, endDate, daysApplied);
    }

    public Long getId() {
        return id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public NoveltyType getNoveltyType() {
        return noveltyType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getDaysApplied() {
        return daysApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Novelty novelty = (Novelty) o;
        return employeeId != null && startDate != null && noveltyType != null
                && employeeId.equals(novelty.employeeId)
                && startDate.equals(novelty.startDate)
                && noveltyType == novelty.noveltyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, startDate, noveltyType);
    }
}
