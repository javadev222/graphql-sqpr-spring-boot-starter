package ru.bank.web.error;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -2720583699106442083L;

    public ResourceNotFoundException(Object fieldValue) {
        super(String.format("Resource not found with id: '%s'", fieldValue));
    }
}
