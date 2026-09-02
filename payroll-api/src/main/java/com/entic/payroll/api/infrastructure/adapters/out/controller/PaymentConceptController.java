package com.entic.payroll.api.infrastructure.adapters.out.controller;

import com.entic.payroll.api.infrastructure.commons.executor.UseCaseHttpExecutor;
import com.entic.payroll.api.infrastructure.dto.paymentconcept.CreatePaymentConceptRequest;
import com.entic.payroll.api.infrastructure.dto.paymentconcept.UpdatePaymentConceptRequest;
import com.entic.payroll.api.infrastructure.mapper.PaymentConceptApiMapper;
import com.entic.payroll.core.application.models.GetPaymentConceptByIdUseCaseIn;
import com.entic.payroll.core.application.port.in.CreatePaymentConceptUseCase;
import com.entic.payroll.core.application.port.in.GetPaymentConceptByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdatePaymentConceptUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payment-concepts")
public class PaymentConceptController {

    private static final Logger log = LoggerFactory.getLogger(PaymentConceptController.class);

    private final UseCaseHttpExecutor useCaseHttpExecutor;
    private final CreatePaymentConceptUseCase createPaymentConceptUseCase;
    private final GetPaymentConceptByIdUseCase getPaymentConceptByIdUseCase;
    private final UpdatePaymentConceptUseCase updatePaymentConceptUseCase;

    public PaymentConceptController(UseCaseHttpExecutor useCaseHttpExecutor,
                                    CreatePaymentConceptUseCase createPaymentConceptUseCase,
                                    GetPaymentConceptByIdUseCase getPaymentConceptByIdUseCase,
                                    UpdatePaymentConceptUseCase updatePaymentConceptUseCase) {
        this.useCaseHttpExecutor = useCaseHttpExecutor;
        this.createPaymentConceptUseCase = createPaymentConceptUseCase;
        this.getPaymentConceptByIdUseCase = getPaymentConceptByIdUseCase;
        this.updatePaymentConceptUseCase = updatePaymentConceptUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePaymentConceptRequest request) {
        log.info("Received POST /payment-concepts for code: {}", request.code());
        return this.useCaseHttpExecutor.execute(createPaymentConceptUseCase,
                PaymentConceptApiMapper.toCommand(request),
                out -> {
                    URI location = URI.create("/payment-concepts/" + out.paymentConceptCreated().id());
                    return ResponseEntity.status(HttpStatus.CREATED).location(location).body(out.paymentConceptCreated());
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received GET /payment-concepts/{}", id);
        return this.useCaseHttpExecutor.execute(getPaymentConceptByIdUseCase, new GetPaymentConceptByIdUseCaseIn(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdatePaymentConceptRequest request) {
        log.info("Received PUT /payment-concepts/{}", id);
        return this.useCaseHttpExecutor.execute(updatePaymentConceptUseCase, PaymentConceptApiMapper.toCommand(id, request));
    }
}
