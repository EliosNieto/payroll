package com.entic.payroll.api.infrastructure.adapters.out.controller;

import com.entic.payroll.api.infrastructure.dto.company.CompanyResponse;
import com.entic.payroll.api.infrastructure.dto.company.CreateCompanyRequest;
import com.entic.payroll.api.infrastructure.mapper.CompanyApiMapper;
import com.entic.payroll.core.application.company.CreateCompanyCommand;
import com.entic.payroll.core.application.company.CreateCompanyService;
import com.entic.payroll.core.domain.company.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private final CreateCompanyService createCompanyService;

    public CompanyController(CreateCompanyService createCompanyService) {
        this.createCompanyService = createCompanyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody CreateCompanyRequest request) {
        log.info("Received POST /companies with NIT: {}", request.nit());

        CreateCompanyCommand command = CompanyApiMapper.toCommand(request);
        Company company = createCompanyService.execute(command);
        CompanyResponse response = CompanyApiMapper.toResponse(company);

        URI location = URI.create("/companies/" + response.id());
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(response);
    }
}
