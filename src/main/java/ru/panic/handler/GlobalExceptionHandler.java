package ru.panic.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handlePermissionDeniedException(HttpServletRequest request, IllegalArgumentException exception) {
        return Map.ofEntries(
                Map.entry("error", "Conflict"),
                Map.entry("message", exception.getMessage()),
                Map.entry("path", request.getServletPath())
        );
    }

}
