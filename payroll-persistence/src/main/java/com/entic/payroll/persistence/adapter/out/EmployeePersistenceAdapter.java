package com.entic.payroll.persistence.adapter.out;

import com.entic.payroll.core.application.port.out.EmployeeRepositoryPort;
import com.entic.payroll.core.domain.employee.Employee;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.persistence.mapper.EmployeeMapper;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import com.entic.payroll.persistence.repository.EmployeeJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(EmployeePersistenceAdapter.class);

    private final EmployeeJpaRepository repository;
    private final CompanyJpaRepository companyRepository;

    public EmployeePersistenceAdapter(EmployeeJpaRepository repository, CompanyJpaRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Employee save(Employee employee) {
        log.debug("Saving employee with document number: {} in company: {}", employee.getDocumentNumber(), employee.getCompanyId());
        com.entic.payroll.persistence.entity.Company company = companyRepository.findById(employee.getCompanyId())
                .orElseThrow(() -> new NotFoundException("companyId", employee.getCompanyId().toString(), "employee.company.notFound"));
        com.entic.payroll.persistence.entity.Employee entity = EmployeeMapper.toEntity(employee, company);
        com.entic.payroll.persistence.entity.Employee saved = repository.save(entity);
        return EmployeeMapper.toDomain(saved);
    }

    @Override
    public Employee findById(Long id) {
        log.debug("Finding employee by id: {}", id);
        com.entic.payroll.persistence.entity.Employee entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", id.toString(), "employee.notFound"));
        return EmployeeMapper.toDomain(entity);
    }

    @Override
    public boolean companyExists(UUID companyId) {
        log.debug("Checking if company exists with id: {}", companyId);
        return companyRepository.existsById(companyId);
    }

    @Override
    public boolean existsByCompanyIdAndDocumentNumber(UUID companyId, String documentNumber) {
        log.debug("Checking if employee exists with document number: {} in company: {}", documentNumber, companyId);
        return repository.existsByCompanyIdAndDocumentNumber(companyId, documentNumber);
    }

    @Override
    public boolean existsByCompanyIdAndDocumentNumberExcludingId(UUID companyId, String documentNumber, Long id) {
        log.debug("Checking if employee exists with document number: {} in company: {} excluding id: {}", documentNumber, companyId, id);
        return repository.existsByCompanyIdAndDocumentNumberAndIdNot(companyId, documentNumber, id);
    }
}