package com.entic.payroll.core.domain.employee;

import com.entic.payroll.core.domain.enums.DocumentType;
import com.entic.payroll.core.domain.enums.WorkerType;

import java.util.Objects;
import java.util.UUID;

public class Employee {

    private final Long id;
    private final UUID companyId;
    private final DocumentType documentType;
    private final String documentNumber;
    private final String firstNames;
    private final String lastNames;
    private final WorkerType workerType;

    private Employee(Long id, UUID companyId, DocumentType documentType, String documentNumber,
                     String firstNames, String lastNames, WorkerType workerType) {
        this.id = id;
        this.companyId = companyId;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
        this.workerType = workerType;
    }

    public static Employee create(UUID companyId, DocumentType documentType, String documentNumber,
                                  String firstNames, String lastNames, WorkerType workerType) {
        return new Employee(null, companyId, documentType, documentNumber, firstNames, lastNames, workerType);
    }

    public static Employee reconstitute(Long id, UUID companyId, DocumentType documentType, String documentNumber,
                                        String firstNames, String lastNames, WorkerType workerType) {
        return new Employee(id, companyId, documentType, documentNumber, firstNames, lastNames, workerType);
    }

    public Employee update(DocumentType documentType, String documentNumber,
                           String firstNames, String lastNames, WorkerType workerType) {
        return new Employee(this.id, this.companyId, documentType, documentNumber, firstNames, lastNames, workerType);
    }

    public Long getId() {
        return id;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public String getLastNames() {
        return lastNames;
    }

    public WorkerType getWorkerType() {
        return workerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return companyId != null && documentNumber != null
                && companyId.equals(employee.companyId)
                && documentNumber.equals(employee.documentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, documentNumber);
    }
}