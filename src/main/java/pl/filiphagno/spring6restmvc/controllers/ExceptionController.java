package pl.filiphagno.spring6restmvc.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> handleBadRequestException(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors().
                stream()
                .map( error -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(error.getField(), error.getDefaultMessage());
                    return errorMap;
                }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<List<Map<String, String>>> handleTransactionSystemException(TransactionSystemException exception) {
        if (exception.getCause().getCause() instanceof ConstraintViolationException constraintViolationException) {
            List<Map<String, String>> errors = constraintViolationException.getConstraintViolations().stream()
                    .map( violation -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
                        return errorMap;
                    })
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.badRequest().build();
    }
}
