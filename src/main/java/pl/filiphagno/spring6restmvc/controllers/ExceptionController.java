package pl.filiphagno.spring6restmvc.controllers;

import org.springframework.http.ResponseEntity;
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
}
