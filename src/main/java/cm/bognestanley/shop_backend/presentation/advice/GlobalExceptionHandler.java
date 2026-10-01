package cm.bognestanley.shop_backend.presentation.advice;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.user.exception.BadUserCredentials;
import cm.bognestanley.shop_backend.infrastructure.exception.StorageException;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDataWrapper<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ErrorDataWrapper<Map<String, String>> error = ErrorDataWrapper.error(errors, "VALIDATION_ERROR",
                "Validation error");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDataWrapper<Map<String, String>>> handleMethodValidation(
            HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(result -> {
            String parameter = result.getMethodParameter().getParameterName();
            String message = result.getResolvableErrors().isEmpty()
                    ? "Invalid value"
                    : result.getResolvableErrors().getFirst().getDefaultMessage();
            errors.put(parameter == null ? "request" : parameter, message);
        });
        return ResponseEntity.badRequest().body(ErrorDataWrapper.error(
                errors, "VALIDATION_ERROR", "Validation error"));
    }

    /**
     * Handles method constraints when Spring's AOP method validation is active.
     * Unlike {@link HandlerMethodValidationException}, this exception is raised
     * before the MVC argument resolver when a controller is proxied.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDataWrapper<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            String parameter = path.substring(path.lastIndexOf('.') + 1);
            errors.put(parameter, violation.getMessage());
        });
        return ResponseEntity.badRequest().body(ErrorDataWrapper.error(
                errors, "VALIDATION_ERROR", "Validation error"));
    }

    @ExceptionHandler({
            MissingRequestHeaderException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorDataWrapper<Map<String, String>>> handleMalformedRequest(Exception ex) {
        Map<String, String> errors = Map.of("request", "Request is malformed or contains an invalid value");
        return ResponseEntity.badRequest().body(ErrorDataWrapper.error(
                errors, "VALIDATION_ERROR", "Validation error"));
    }

    @ExceptionHandler(DomainErrorException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleDomainError(DomainErrorException ex) {
        Integer status = ex.getErrorCode().getHttpStatusCode() != null
                ? ex.getErrorCode().getHttpStatusCode()
                : HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status)
                .body(ErrorDataWrapper.error(ex.getMessage(), ex.getErrorCode().getCode()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleApplicationException(ApplicationException ex) {
        Integer status = ex.getErrorCode().getHttpStatusCode() != null
                ? ex.getErrorCode().getHttpStatusCode()
                : HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status)
                .body(ErrorDataWrapper.error(ex.getMessage(), ex.getErrorCode().getCode()));
    }

    @ExceptionHandler(BadUserCredentials.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleBadCredentials(BadUserCredentials ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDataWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleStorageException(StorageException ex) {
        log.warn("Storage operation rejected: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDataWrapper.error(null, "STORAGE_ERROR", "Unable to process the uploaded file"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleUsernameNotFound(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDataWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ErrorDataWrapper.error(null, "MAX_UPLOAD_SIZE_ERROR", "Uploaded file is too large"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDataWrapper.empty());
    }

}
