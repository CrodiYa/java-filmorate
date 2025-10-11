package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

        log.warn("Resolved: [{}]", ex.getClass().getName());
        log.debug("Validation error", ex);
        Map<String, String> errors = new HashMap<>();


        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(
                new ApiError("Bad Request",
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        errors)
        );
    }

    /**
     * Handles malformed JSON requests and invalid data formats.
     * Provides specific parsing error details when available.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("Resolved: [{}]", ex.getClass().getName());
        log.debug("Invalid request format", ex);

        return ResponseEntity.badRequest().body(
                new ApiError("Bad Request",
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        getErrors(ex)
                )
        );
    }

    /**
     * Helper for HttpMessageNotReadableException handler
     * Extracts meaningful error messages from JSON parsing exceptions.
     *
     * @return map with error
     */
    private static Map<String, String> getErrors(HttpMessageNotReadableException ex) {
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
     * Handles business logic "not found" scenarios.
     * Returns 404 status with descriptive message.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {

        log.warn("Resolved: [{}]", ex.getClass().getName());
        log.debug("Not Found", ex);
        Map<String, String> errors = new HashMap<>();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiError("Not Found",
                        HttpStatus.NOT_FOUND.value(),
                        request.getRequestURI(),
                        Map.of("error", ex.getMessage())
                )
        );
    }

    /**
     * Handles custom validation exceptions from service layer.
     * Returns 400 status with business rule violation details.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        log.warn("Resolved: [{}]", ex.getClass().getName());
        log.debug("Validation Error", ex);

        return ResponseEntity.badRequest().body(
                new ApiError("Bad Request",
                        HttpStatus.BAD_REQUEST.value(),
                        request.getRequestURI(),
                        Map.of("error", ex.getMessage())

                )
        );
    }
}
