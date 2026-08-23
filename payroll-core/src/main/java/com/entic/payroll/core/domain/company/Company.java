package com.entic.payroll.core.domain.company;

import java.util.Objects;
import java.util.UUID;

public class Company {

    private final UUID id;
    private final String nit;
    private final String legalName;
    private final boolean payrollTaxExempt;
    private final boolean active;

    private Company(UUID id, String nit, String legalName, boolean payrollTaxExempt, boolean active) {
        this.id = id;
        this.nit = nit;
        this.legalName = legalName;
        this.payrollTaxExempt = payrollTaxExempt;
        this.active = active;
    }

    public static Company create(String nit, String legalName, boolean payrollTaxExempt) {
        return new Company(null, nit, legalName, payrollTaxExempt, true);
    }

    public static Company reconstitute(UUID id, String nit, String legalName, boolean payrollTaxExempt, boolean active) {
        return new Company(id, nit, legalName, payrollTaxExempt, active);
    }

    public Company update(String nit, String legalName, boolean payrollTaxExempt) {
        return new Company(this.id, nit, legalName, payrollTaxExempt, this.active);
    }

    public UUID getId() {
        return id;
    }

    public String getNit() {
        return nit;
    }

    public String getLegalName() {
        return legalName;
    }

    public boolean isPayrollTaxExempt() {
        return payrollTaxExempt;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return nit != null && nit.equals(company.nit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nit);
    }
}
