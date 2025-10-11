package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Custom error response body for API exceptions.
 *
 * <p>Contains structured error information returned to clients when exceptions occur.
 */
@Data
public class ApiError {
    private String message;
    private int status;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
    private String path;

    /**
     * Creates a new API error response.
     *
     * @param message high-level error description
     * @param status HTTP status code
     * @param path request URI that caused the error
     * @param errors detailed field-specific error messages
     */
    public ApiError(String message, int status, String path, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.status = status;
        this.errors = errors;
        this.path = path;
    }
}
