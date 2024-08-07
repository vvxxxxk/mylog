package com.golym.mylog.common.exception;

import com.golym.mylog.model.dto.common.ErrorMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    //    +------------------------------------------------------------------+
    //    |                        4xx Client Errors                         |
    //    +------------------------------------------------------------------+
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleException(BadRequestException e) {
        log.error(e.getMessage());

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleException(ConflictException e) {
        log.error(e.getMessage());

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage());

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .status(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .message("Method Not Allowed")
                .build();

        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> toSnakeCase(fieldError.getField()) + " : " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .build();

        log.debug("{}", errorMessage);

        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.BAD_REQUEST);
    }

    //    +------------------------------------------------------------------+
    //    |                        5xx Server Errors                         |
    //    +------------------------------------------------------------------+
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
        log.error(e.getMessage(), e);

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Internal Server Error")
                .build();

        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);

        ErrorMessageDto errorMessage = ErrorMessageDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Internal Server Error")
                .build();
        return new ResponseEntity<>(errorMessage, getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Content-Type-Options", "nosniff");
        return headers;
    }

    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
