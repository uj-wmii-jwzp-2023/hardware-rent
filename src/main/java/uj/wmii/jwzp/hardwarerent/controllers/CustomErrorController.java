package uj.wmii.jwzp.hardwarerent.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundErrors(NotFoundException notFoundException) {
        String message = notFoundException.getMessage();

        return ResponseEntity.status(404).body(message);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistsErrors(AlreadyExistsException exception) {
        String message = exception.getMessage();

        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBindErrors(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream().map(
                fieldError -> {
                    Map<String, String> errorMessage = new HashMap<>();
                    errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMessage;
                }
        ).toList();
        return ResponseEntity.status(400).body(errors);
    }
}
