package com.entic.payroll.api.infrastructure.adapters.out.controller;

import com.entic.payroll.api.infrastructure.commons.executor.UseCaseHttpExecutor;
import com.entic.payroll.api.infrastructure.dto.contract.CreateContractRequest;
import com.entic.payroll.api.infrastructure.dto.contract.UpdateContractRequest;
import com.entic.payroll.api.infrastructure.mapper.ContractApiMapper;
import com.entic.payroll.core.application.models.GetContractByIdUseCaseIn;
import com.entic.payroll.core.application.port.in.CreateContractUseCase;
import com.entic.payroll.core.application.port.in.GetContractByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateContractUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    private static final Logger log = LoggerFactory.getLogger(ContractController.class);

    private final UseCaseHttpExecutor useCaseHttpExecutor;
    private final CreateContractUseCase createContractUseCase;
    private final GetContractByIdUseCase getContractByIdUseCase;
    private final UpdateContractUseCase updateContractUseCase;

    public ContractController(UseCaseHttpExecutor useCaseHttpExecutor,
                              CreateContractUseCase createContractUseCase,
                              GetContractByIdUseCase getContractByIdUseCase,
                              UpdateContractUseCase updateContractUseCase) {
        this.useCaseHttpExecutor = useCaseHttpExecutor;
        this.createContractUseCase = createContractUseCase;
        this.getContractByIdUseCase = getContractByIdUseCase;
        this.updateContractUseCase = updateContractUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateContractRequest request) {
        log.info("Received POST /contracts for employee: {}", request.employeeId());
        return this.useCaseHttpExecutor.execute(createContractUseCase,
                ContractApiMapper.toCommand(request),
                out -> {
                    URI location = URI.create("/contracts/" + out.contractCreated().id());
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .location(location)
                            .body(out.contractCreated());
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received GET /contracts/{}", id);
        return this.useCaseHttpExecutor.execute(getContractByIdUseCase, new GetContractByIdUseCaseIn(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateContractRequest request) {
        log.info("Received PUT /contracts/{}", id);
        return this.useCaseHttpExecutor.execute(updateContractUseCase,
                ContractApiMapper.toCommand(id, request));
    }
}
