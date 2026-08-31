package com.entic.payroll.persistence.adapter.out;

import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.persistence.entity.Company;
import com.entic.payroll.persistence.entity.Employee;
import com.entic.payroll.persistence.mapper.ContractMapper;
import com.entic.payroll.persistence.repository.CompanyJpaRepository;
import com.entic.payroll.persistence.repository.ContractJpaRepository;
import com.entic.payroll.persistence.repository.EmployeeJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ContractPersistenceAdapter implements ContractRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(ContractPersistenceAdapter.class);

    private final ContractJpaRepository repository;
    private final CompanyJpaRepository companyRepository;
    private final EmployeeJpaRepository employeeRepository;

    public ContractPersistenceAdapter(ContractJpaRepository repository,
                                      CompanyJpaRepository companyRepository,
                                      EmployeeJpaRepository employeeRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Contract save(Contract contract) {
        log.debug("Saving contract for employee: {} in company: {}",
                contract.getEmployeeId(), contract.getCompanyId());
        Company company = companyRepository.findById(contract.getCompanyId())
                .orElseThrow(() -> new NotFoundException("companyId",
                        contract.getCompanyId().toString(), "contract.company.notFound"));
        Employee employee = employeeRepository.findById(contract.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("employeeId",
                        contract.getEmployeeId().toString(), "contract.employee.notFound"));
        com.entic.payroll.persistence.entity.Contract entity =
                ContractMapper.toEntity(contract, company, employee);
        com.entic.payroll.persistence.entity.Contract saved = repository.save(entity);
        return ContractMapper.toDomain(saved);
    }

    @Override
    public Contract findById(Long id) {
        log.debug("Finding contract by id: {}", id);
        com.entic.payroll.persistence.entity.Contract entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", id.toString(), "contract.notFound"));
        return ContractMapper.toDomain(entity);
    }

    @Override
    public boolean companyExists(UUID companyId) {
        log.debug("Checking if company exists with id: {}", companyId);
        return companyRepository.existsById(companyId);
    }

    @Override
    public boolean employeeExists(Long employeeId) {
        log.debug("Checking if employee exists with id: {}", employeeId);
        return employeeRepository.existsById(employeeId);
    }

    @Override
    public boolean existsActiveForEmployee(Long employeeId, Long excludingId) {
        log.debug("Checking if employee {} has an active contract excluding id: {}", employeeId, excludingId);
        if (excludingId == null) {
            return repository.existsByEmployeeIdAndEndDateIsNull(employeeId);
        }
        return repository.existsByEmployeeIdAndEndDateIsNullAndIdNot(employeeId, excludingId);
    }
}
