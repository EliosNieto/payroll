package com.entic.payroll.persistence.adapter.out;

import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.novelty.Novelty;
import com.entic.payroll.persistence.entity.Employee;
import com.entic.payroll.persistence.mapper.NoveltyMapper;
import com.entic.payroll.persistence.repository.EmployeeJpaRepository;
import com.entic.payroll.persistence.repository.NoveltyJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoveltyPersistenceAdapter implements NoveltyRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(NoveltyPersistenceAdapter.class);

    private final NoveltyJpaRepository repository;
    private final EmployeeJpaRepository employeeRepository;

    public NoveltyPersistenceAdapter(NoveltyJpaRepository repository,
                                     EmployeeJpaRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Novelty save(Novelty novelty) {
        log.debug("Saving novelty for employee: {} of type: {}", novelty.getEmployeeId(), novelty.getNoveltyType());
        Employee employee = employeeRepository.findById(novelty.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("employeeId",
                        novelty.getEmployeeId().toString(), "novelty.employee.notFound"));
        com.entic.payroll.persistence.entity.Novelty entity =
                NoveltyMapper.toEntity(novelty, employee);
        com.entic.payroll.persistence.entity.Novelty saved = repository.save(entity);
        return NoveltyMapper.toDomain(saved);
    }

    @Override
    public Novelty findById(Long id) {
        log.debug("Finding novelty by id: {}", id);
        com.entic.payroll.persistence.entity.Novelty entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("id", id.toString(), "novelty.notFound"));
        return NoveltyMapper.toDomain(entity);
    }

    @Override
    public boolean employeeExists(Long employeeId) {
        log.debug("Checking if employee exists with id: {}", employeeId);
        return employeeRepository.existsById(employeeId);
    }
}
