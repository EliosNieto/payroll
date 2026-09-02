package com.entic.payroll.core.domain.paymentconcept;

import com.entic.payroll.core.domain.enums.ConceptNature;

import java.util.Objects;
import java.util.UUID;

public class PaymentConcept {

    private final Long id;
    private final UUID companyId;
    private final String code;
    private final String name;
    private final ConceptNature nature;

    private PaymentConcept(Long id, UUID companyId, String code, String name, ConceptNature nature) {
        this.id = id;
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.nature = nature;
    }

    public static PaymentConcept create(UUID companyId, String code, String name, ConceptNature nature) {
        return new PaymentConcept(null, companyId, code, name, nature);
    }

    public static PaymentConcept reconstitute(Long id, UUID companyId, String code, String name,
                                              ConceptNature nature) {
        return new PaymentConcept(id, companyId, code, name, nature);
    }

    public PaymentConcept update(String code, String name, ConceptNature nature) {
        return new PaymentConcept(this.id, this.companyId, code, name, nature);
    }

    public Long getId() {
        return id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public ConceptNature getNature() {
        return nature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentConcept that = (PaymentConcept) o;
        return code != null && Objects.equals(companyId, that.companyId)
                && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, code);
    }
}
