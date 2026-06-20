package cm.bognestanley.shop_backend.presentation.advice;

import cm.bognestanley.shop_backend.application.common.exception.ApplicationException;
import cm.bognestanley.shop_backend.domain.common.exception.DomainErrorException;
import cm.bognestanley.shop_backend.domain.user.exception.BadUserCredentials;
import cm.bognestanley.shop_backend.infrastructure.exception.StorageException;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDataWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleUsernameNotFound(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDataWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDataWrapper.error(ex.getMessage()));
    }
}
