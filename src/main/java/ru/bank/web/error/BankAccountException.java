package ru.bank.web.error;

public class BankAccountException extends RuntimeException {
    private static final long serialVersionUID = 4863053508827842926L;

    public BankAccountException(String message) {
        super(message);
    }
}
