package com.entic.payroll.api.infrastructure.adapters.out.advice;

import com.entic.payroll.api.infrastructure.dto.errors.ErrorFieldResponse;
import com.entic.payroll.core.application.commons.errors.ApplicationErrorSpec;
import com.entic.payroll.core.domain.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String resolveMessage(ApplicationErrorSpec ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String resolved = messageSource.getMessage(ex.getMessage(), ex.messageArgs(), null, locale);
        if (resolved != null) return resolved;
        resolved = messageSource.getMessage("error." + ex.errorCode(), ex.messageArgs(), null, locale);
        if (resolved != null) return resolved;
        return ex.getMessage();
    }

    private String resolveKey(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));
        return headers;
    }

    private ResponseEntity<ErrorFieldResponse> build(HttpStatus status, String errorKey,
                                                     Map<String, Object> metadata, ApplicationErrorSpec ex) {
        return ResponseEntity.status(status)
                .headers(jsonHeaders())
                .body(new ErrorFieldResponse(metadata, errorKey, resolveMessage(ex)));
    }

    private ResponseEntity<ErrorFieldResponse> build(HttpStatus status, String errorKey,
                                                     Map<String, Object> metadata, String key, Object... args) {
        return ResponseEntity.status(status)
                .headers(jsonHeaders())
                .body(new ErrorFieldResponse(metadata, errorKey, resolveKey(key, args)));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorFieldResponse> handle(ValidationException ex) {
        log.warn("Validation error: {} - {}", ex.errorCode(), ex.getMessage());
        return build(HttpStatus.CONFLICT, ex.errorCode(), ex.metadata(), ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorFieldResponse> handle(NotFoundException ex) {
        log.warn("Not found: {} - {}", ex.errorCode(), ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.errorCode(), ex.metadata(), ex);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorFieldResponse> handle(AlreadyExistsException ex) {
        log.warn("Already exists: {} - {}", ex.errorCode(), ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.errorCode(), ex.metadata(), ex);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorFieldResponse> handle(AuthException ex) {
        log.warn("Authentication error: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.errorCode(), ex.metadata(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorFieldResponse> handle(MethodArgumentNotValidException ex) {
        log.warn("Method argument validation error");
        return build(HttpStatus.BAD_REQUEST, "validationError", null, "error.validationError");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorFieldResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex
    ) {
        log.warn("Malformed JSON request: {}", ex.getMessage());

        String key = "http.invalid.json";
        if (ex.getMessage() != null &&
                ex.getMessage().contains("Cannot deserialize value of type `java.lang.Long`")) {
            key = "http.invalid.json.long";
        }

        return build(HttpStatus.BAD_REQUEST, "badRequest", null, key);
    }
}
