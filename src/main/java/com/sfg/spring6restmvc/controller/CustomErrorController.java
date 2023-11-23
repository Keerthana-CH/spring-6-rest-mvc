package com.sfg.spring6restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity<?> handleJPAViolations(TransactionSystemException exception){
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();
        if (exception.getCause().getCause() instanceof ConstraintViolationException){
            ConstraintViolationException cve = (ConstraintViolationException) exception.getCause().getCause();
            List errors = cve.getConstraintViolations().stream().map(constraintViolation -> {
                Map<String,String> map = new HashMap<>();
                map.put(constraintViolation.getPropertyPath().toString(),constraintViolation.getMessage());
                return map;
            }).toList();
            return bodyBuilder.body(errors);
        }
        return bodyBuilder.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception){

        List<Map<String, String>> errorList = exception.getFieldErrors().stream().map(fieldError -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            return errorMap;
        }).toList();

        return ResponseEntity.badRequest().body(errorList);
    }
}
