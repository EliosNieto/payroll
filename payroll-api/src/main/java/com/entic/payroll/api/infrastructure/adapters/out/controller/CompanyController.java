package com.entic.payroll.api.infrastructure.adapters.out.controller;

import com.entic.payroll.api.infrastructure.commons.executor.UseCaseHttpExecutor;
import com.entic.payroll.api.infrastructure.dto.company.CreateCompanyRequest;
import com.entic.payroll.api.infrastructure.dto.company.UpdateCompanyRequest;
import com.entic.payroll.api.infrastructure.mapper.CompanyApiMapper;
import com.entic.payroll.core.application.models.GetCompanyByIdUseCaseIn;
import com.entic.payroll.core.application.port.in.CreateCompanyUseCase;
import com.entic.payroll.core.application.port.in.GetCompanyByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateCompanyUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private final UseCaseHttpExecutor useCaseHttpExecutor;
    private final CreateCompanyUseCase createCompanyUseCase;
    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;

    public CompanyController(UseCaseHttpExecutor useCaseHttpExecutor,
                             CreateCompanyUseCase createCompanyUseCase,
                             GetCompanyByIdUseCase getCompanyByIdUseCase,
                             UpdateCompanyUseCase updateCompanyUseCase) {
        this.useCaseHttpExecutor = useCaseHttpExecutor;
        this.createCompanyUseCase = createCompanyUseCase;
        this.getCompanyByIdUseCase = getCompanyByIdUseCase;
        this.updateCompanyUseCase = updateCompanyUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCompanyRequest request) {
        log.info("Received POST /companies with NIT: {}", request.nit());
        return this.useCaseHttpExecutor.execute(createCompanyUseCase,
                          CompanyApiMapper.toCommand(request),
                out ->{
                    URI location = URI.create("/companies/" + out.companyCreated().id());
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .location(location)
                            .body(out.companyCreated());
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        log.info("Received GET /companies/{}", id);
        return this.useCaseHttpExecutor.execute(getCompanyByIdUseCase, new GetCompanyByIdUseCaseIn(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdateCompanyRequest request) {
        log.info("Received PUT /companies/{}", id);
        return this.useCaseHttpExecutor.execute(updateCompanyUseCase,
                CompanyApiMapper.toCommand(id, request));
    }
}
