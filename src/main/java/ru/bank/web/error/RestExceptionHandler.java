package ru.bank.web.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private RestExceptionHandler() {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResult errorResult = new ErrorResult(NOT_FOUND);
        errorResult.setMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @ExceptionHandler(BankAccountException.class)
    protected ResponseEntity<Object> handleBankAccountException(BankAccountException ex) {
        ErrorResult errorResult = new ErrorResult(BAD_REQUEST);
        errorResult.setMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.info("handling entity not found exception");
        ErrorResult errorResult = new ErrorResult(NOT_FOUND);
        errorResult.setMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResult apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ErrorResult(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex) {
        log.info("handling file not found exception");
        ErrorResult errorResult = new ErrorResult(NOT_FOUND);
        errorResult.setMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @ExceptionHandler(value = NoSuchFileException.class)
    public ResponseEntity<Object> handleNoSuchFileException(NoSuchFileException ex) {
        log.info("handling NoSuchFileException");
        ErrorResult errorResult = new ErrorResult(INTERNAL_SERVER_ERROR);
        errorResult.setMessage("No such file: " + ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("handling constraint violation exception: " + ex.getMessage());
        ErrorResult errorResult = new ErrorResult(NOT_FOUND);
        errorResult.setMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        String error = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ErrorResult errorResult = new ErrorResult(BAD_REQUEST);
        errorResult.setMessage(error);
        return buildResponseEntity(errorResult);
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        log.info("handling io exception: " + ex.getMessage());
        log.error(ex.getMessage());
        ErrorResult errorResult = new ErrorResult(BAD_REQUEST);
        errorResult.setMessage("No such file");
        return buildResponseEntity(errorResult);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResult errorResult = new ErrorResult(BAD_REQUEST);
        errorResult.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        errorResult.setDebugMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        return buildResponseEntity(new ErrorResult(HttpStatus.BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResult errorResult = new ErrorResult(BAD_REQUEST);
        errorResult.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        errorResult.setDebugMessage(ex.getMessage());
        return buildResponseEntity(errorResult);
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResult {
        private int code;
        private HttpStatus status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private LocalDateTime timestamp;
        private String message;
        private String debugMessage;

        private ErrorResult() {
            timestamp = LocalDateTime.now();
        }

        ErrorResult(HttpStatus status) {
            this();
            this.status = status;
            this.code = status.value();
        }

        ErrorResult(HttpStatus status, Throwable ex) {
            this();
            this.status = status;
            this.message = "Unexpected error";
            this.debugMessage = ex.getLocalizedMessage();
        }

        ErrorResult(HttpStatus status, String message, Throwable ex) {
            this();
            this.status = status;
            this.message = message;
            this.debugMessage = ex.getLocalizedMessage();
        }
    }
}
