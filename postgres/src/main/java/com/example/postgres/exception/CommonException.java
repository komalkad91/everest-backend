package com.example.postgres.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class CommonException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<String> handleDataNotFoundExceptions(DataNotFound exp) {
        System.out.println(exp.getMsg());

         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMessage());
    }


}
