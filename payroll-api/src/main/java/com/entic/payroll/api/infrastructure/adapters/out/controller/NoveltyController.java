package com.entic.payroll.api.infrastructure.adapters.out.controller;

import com.entic.payroll.api.infrastructure.commons.executor.UseCaseHttpExecutor;
import com.entic.payroll.api.infrastructure.dto.novelty.CreateNoveltyRequest;
import com.entic.payroll.api.infrastructure.dto.novelty.UpdateNoveltyRequest;
import com.entic.payroll.api.infrastructure.mapper.NoveltyApiMapper;
import com.entic.payroll.core.application.models.GetNoveltyByIdUseCaseIn;
import com.entic.payroll.core.application.port.in.CreateNoveltyUseCase;
import com.entic.payroll.core.application.port.in.GetNoveltyByIdUseCase;
import com.entic.payroll.core.application.port.in.UpdateNoveltyUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/novelties")
public class NoveltyController {

    private static final Logger log = LoggerFactory.getLogger(NoveltyController.class);

    private final UseCaseHttpExecutor useCaseHttpExecutor;
    private final CreateNoveltyUseCase createNoveltyUseCase;
    private final GetNoveltyByIdUseCase getNoveltyByIdUseCase;
    private final UpdateNoveltyUseCase updateNoveltyUseCase;

    public NoveltyController(UseCaseHttpExecutor useCaseHttpExecutor,
                             CreateNoveltyUseCase createNoveltyUseCase,
                             GetNoveltyByIdUseCase getNoveltyByIdUseCase,
                             UpdateNoveltyUseCase updateNoveltyUseCase) {
        this.useCaseHttpExecutor = useCaseHttpExecutor;
        this.createNoveltyUseCase = createNoveltyUseCase;
        this.getNoveltyByIdUseCase = getNoveltyByIdUseCase;
        this.updateNoveltyUseCase = updateNoveltyUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateNoveltyRequest request) {
        log.info("Received POST /novelties for employee: {}", request.employeeId());
        return this.useCaseHttpExecutor.execute(createNoveltyUseCase,
                NoveltyApiMapper.toCommand(request),
                out -> {
                    URI location = URI.create("/novelties/" + out.noveltyCreated().id());
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .location(location)
                            .body(out.noveltyCreated());
                });
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Received GET /novelties/{}", id);
        return this.useCaseHttpExecutor.execute(getNoveltyByIdUseCase, new GetNoveltyByIdUseCaseIn(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateNoveltyRequest request) {
        log.info("Received PUT /novelties/{}", id);
        return this.useCaseHttpExecutor.execute(updateNoveltyUseCase,
                NoveltyApiMapper.toCommand(id, request));
    }
}
