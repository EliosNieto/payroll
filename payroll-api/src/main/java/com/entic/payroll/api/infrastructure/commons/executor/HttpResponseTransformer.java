package com.entic.payroll.api.infrastructure.commons.executor;

import org.springframework.http.ResponseEntity;

@FunctionalInterface
public interface HttpResponseTransformer<OUT, RES> {
    ResponseEntity<RES> transform(OUT out);
}
