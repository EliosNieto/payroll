package com.entic.payroll.api.infrastructure.commons.executor;

import com.entic.payroll.core.application.commons.operation.ApplicationRequest;
import com.entic.payroll.core.application.commons.operation.ApplicationResponse;
import com.entic.payroll.core.application.commons.operation.ApplicationUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UseCaseHttpExecutor {

    @Transactional
    public <IN extends ApplicationRequest, OUT extends ApplicationResponse> ResponseEntity<OUT> execute(
            ApplicationUseCase<IN, OUT> useCase,
            IN request
    ) {
        OUT out = useCase.execute(request);
        return ResponseEntity.ok(out);
    }

    @Transactional
    public <IN extends ApplicationRequest, OUT extends ApplicationResponse, RES> ResponseEntity<RES> execute(
            ApplicationUseCase<IN, OUT> useCase,
            IN request,
            HttpResponseTransformer<OUT, RES> transformer
    ) {
        OUT out = useCase.execute(request);
        return transformer.transform(out);
    }
}
