package cm.bognestanley.shop_backend.presentation.advice;


import cm.bognestanley.shop_backend.domain.user.exception.EmailAlreadyExistsException;
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
    public ResponseEntity<ErrorDataWrapper<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ErrorDataWrapper<Map<String, String>> error = ErrorDataWrapper.error(errors, "VALIDATION_ERROR", "Validation error");
        return ResponseEntity.badRequest().body(error);
    }

    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleUsernameNotFound(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDataWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorDataWrapper.error(ex.getMessage(), "EMAIL_ALREADY_EXISTS"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDataWrapper<?>> handleException(Exception ex){
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDataWrapper.error(ex.getMessage()));
    }
}
