package com.entic.payroll.core.domain.contract;

import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.core.domain.enums.RiskLevel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Contract {

    private final Long id;
    private final UUID companyId;
    private final Long employeeId;
    private final ContractType contractType;
    private final BigDecimal baseSalary;
    private final boolean integralSalary;
    private final RiskLevel riskLevelArl;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Contract(Long id, UUID companyId, Long employeeId, ContractType contractType,
                     BigDecimal baseSalary, boolean integralSalary, RiskLevel riskLevelArl,
                     LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.companyId = companyId;
        this.employeeId = employeeId;
        this.contractType = contractType;
        this.baseSalary = baseSalary;
        this.integralSalary = integralSalary;
        this.riskLevelArl = riskLevelArl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Contract create(UUID companyId, Long employeeId, ContractType contractType,
                                  BigDecimal baseSalary, boolean integralSalary, RiskLevel riskLevelArl,
                                  LocalDate startDate, LocalDate endDate) {
        return new Contract(null, companyId, employeeId, contractType, baseSalary, integralSalary,
                riskLevelArl, startDate, endDate);
    }

    public static Contract reconstitute(Long id, UUID companyId, Long employeeId, ContractType contractType,
                                        BigDecimal baseSalary, boolean integralSalary, RiskLevel riskLevelArl,
                                        LocalDate startDate, LocalDate endDate) {
        return new Contract(id, companyId, employeeId, contractType, baseSalary, integralSalary,
                riskLevelArl, startDate, endDate);
    }

    public Contract update(ContractType contractType, BigDecimal baseSalary, boolean integralSalary,
                           RiskLevel riskLevelArl, LocalDate startDate, LocalDate endDate) {
        return new Contract(this.id, this.companyId, this.employeeId, contractType, baseSalary,
                integralSalary, riskLevelArl, startDate, endDate);
    }

    public boolean isActive() {
        return endDate == null;
    }

    public Long getId() {
        return id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public boolean isIntegralSalary() {
        return integralSalary;
    }

    public RiskLevel getRiskLevelArl() {
        return riskLevelArl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return companyId != null && employeeId != null && startDate != null
                && companyId.equals(contract.companyId)
                && employeeId.equals(contract.employeeId)
                && startDate.equals(contract.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, employeeId, startDate);
    }
}
