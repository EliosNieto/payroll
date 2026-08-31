package com.entic.payroll.api.infrastructure.adapters.out.controller;

import com.entic.payroll.api.infrastructure.commons.executor.UseCaseHttpExecutor;
import com.entic.payroll.api.infrastructure.dto.employee.CreateEmployeeRequest;
import com.entic.payroll.api.infrastructure.dto.employee.UpdateEmployeeRequest;
import com.entic.payroll.api.infrastructure.mapper.EmployeeApiMapper;
import com.entic.payroll.core.application.models.GetEmployeeByIdUseCaseIn;
import com.entic.payroll.core.application.port.in.CreateEmployeeUseCase;
import com.entic.payroll.core.application.port.in.GetEmployeeByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateEmployeeUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final UseCaseHttpExecutor useCaseHttpExecutor;
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetEmployeeByIdUseCase getEmployeeByIdUseCase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;

    public EmployeeController(UseCaseHttpExecutor useCaseHttpExecutor,
                              CreateEmployeeUseCase createEmployeeUseCase,
                              GetEmployeeByIdUseCase getEmployeeByIdUseCase,
                              UpdateEmployeeUseCase updateEmployeeUseCase) {
        this.useCaseHttpExecutor = useCaseHttpExecutor;
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.getEmployeeByIdUseCase = getEmployeeByIdUseCase;
        this.updateEmployeeUseCase = updateEmployeeUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateEmployeeRequest request) {
        log.info("Received POST /employees with document number: {}", request.documentNumber());
        return this.useCaseHttpExecutor.execute(createEmployeeUseCase,
                EmployeeApiMapper.toCommand(request),
                out -> {
                    URI location = URI.create("/employees/" + out.employeeCreated().id());
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .location(location)
                            .body(out.employeeCreated());
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received GET /employees/{}", id);
        return this.useCaseHttpExecutor.execute(getEmployeeByIdUseCase, new GetEmployeeByIdUseCaseIn(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateEmployeeRequest request) {
        log.info("Received PUT /employees/{}", id);
        return this.useCaseHttpExecutor.execute(updateEmployeeUseCase,
                EmployeeApiMapper.toCommand(id, request));
    }
}