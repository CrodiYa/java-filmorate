package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers.
 * Centralizes exception handling and provides consistent error responses.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from @Valid annotated parameters.
     * Returns field-specific error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        logInfo(ex, "Validation error from handleHttpMessageNotReadable");
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return createBadRequest(request.getRequestURI(), errors);
    }

    /**
     * Handles malformed JSON requests and invalid data formats.
     * Provides specific parsing error details when available.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        logInfo(ex, "Invalid request format from handleHttpMessageNotReadable");
        return createBadRequest(request.getRequestURI(), getErrors(ex));
    }

    /**
     * Handles business logic "not found" scenarios.
     * Returns 404 status with descriptive message.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {

        logInfo(ex, "Not Found from handleNotFoundException");

        return createResponseEntity(
                HttpStatus.NOT_FOUND,
                request.getRequestURI(),
                Map.of("error", ex.getMessage()));
    }

    /**
     * Handles custom validation exceptions from service layer.
     * Returns 400 status with business rule violation details.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        logInfo(ex, "Validation Error from handleValidationException");
        return createBadRequest(request.getRequestURI(), Map.of("error", ex.getMessage()));
    }

    /**
     * Handles custom validation exceptions from service layer.
     * Returns 400 status without details dut to security reasons.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleMismatchAndConstraintViolation(
            Exception ex,
            HttpServletRequest request) {

        logInfo(ex, "Bad Request Error from handleMismatchAndConstraintViolation");
        return createBadRequest(request.getRequestURI(), Map.of("error", "Invalid request format"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        logInfo(ex, "Handling DataIntegrityViolationException");
        String msg = ex.getMessage().toLowerCase();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMsg = "Invalid request format";

        if (msg.contains("foreign key")) {

            if (msg.contains("mpa")) {
                errorMsg = "Mpa не найден";
                status = HttpStatus.NOT_FOUND;
            } else if (msg.contains("genre")) {
                errorMsg = "Один из указанных жанров не найден";
                status = HttpStatus.NOT_FOUND;
            }
        }

        return createResponseEntity(
                status,
                request.getRequestURI(),
                Map.of("error", errorMsg));
    }

    /**
     * Helper for HttpMessageNotReadableException handler
     * <p>Extracts meaningful error messages from JSON parsing exceptions.
     *
     * @return map with errors
     */
    private Map<String, String> getErrors(HttpMessageNotReadableException ex) {
        String message = "Invalid request format";

        if (ex.getCause() instanceof JsonParseException jpe) {
            message = String.format("%s at line: %d, column: %d",
                    jpe.getOriginalMessage(),
                    jpe.getLocation().getLineNr(),
                    jpe.getLocation().getColumnNr()
            );
        } else if (ex.getCause() instanceof InvalidFormatException) {
            message = "Invalid data format";
        }

        return Map.of("error", message);
    }

    /**
     * Helper method to construct a ResponseEntity with ApiError as body
     */
    private ResponseEntity<ApiError> createResponseEntity(HttpStatus status, String URI, Map<String, String> errors) {
        return ResponseEntity.status(status).body(
                new ApiError(
                        status.getReasonPhrase(),
                        status.value(),
                        URI,
                        errors
                )
        );
    }

    /**
     * Helper method to construct a BadRequest ResponseEntity.
     */
    private ResponseEntity<ApiError> createBadRequest(String path, Map<String, String> errors) {
        return createResponseEntity(
                HttpStatus.BAD_REQUEST,
                path,
                errors);
    }

    /**
     * Helper method to log info.
     */
    private void logInfo(Throwable ex, String info) {
        log.info("Resolved: [{}] Info: [{}]", ex.getClass().getName(), info);
        log.debug(info, ex);
    }
}
