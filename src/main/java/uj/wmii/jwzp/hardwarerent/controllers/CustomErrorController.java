package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uj.wmii.jwzp.hardwarerent.exceptions.*;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
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

    @ExceptionHandler(CategoryRemovalException.class)
    public ResponseEntity<String> handleCategoryRemovalErrors(CategoryRemovalException exception) {
        String message = exception.getMessage();

        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(InvalidDatesException.class)
    public ResponseEntity<String> handleInvalidDatesErrors(InvalidDatesException exception) {
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

    @ExceptionHandler(NotExistingUserException.class)
    public ResponseEntity<String> handleLoginErrors(NotExistingUserException exception) {
        String message = exception.getMessage();

        return ResponseEntity.status(403).body(message);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDatesParseErrors(DateTimeParseException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(NoEnoughMoneyException.class)
    public ResponseEntity<String> handleNoMoneyErrors(NoEnoughMoneyException exception) {
        String message = exception.getMessage();

        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(ProductAlreadyInOrderException.class)
    public ResponseEntity<String> handleProductConflicErrors(ProductAlreadyInOrderException exception) {
        String message = exception.getMessage();

        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(ChangeOrderStatusNotApplicableException.class)
    public ResponseEntity<String> handleOrderStatusErros(ChangeOrderStatusNotApplicableException exception) {
        String message = exception.getMessage();

        return ResponseEntity.status(400).body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleEnumErrors(IllegalArgumentException exception) {
        return ResponseEntity.status(400).body("Only possible Enum values are INITIALIZED, CREATED, FINISHED and CANCELED!");
    }

    @ExceptionHandler(OrderStatusNotValidException.class)
    public ResponseEntity<String> handleOrderStatusNotValidErrors(OrderStatusNotValidException exception) {
        String message = exception.getMessage();
        return ResponseEntity.status(400).body(message);
    }
}
