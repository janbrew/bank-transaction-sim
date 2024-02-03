package net.jsf.exceptions;

public class InsufficientAccountBalance extends Exception {
    public InsufficientAccountBalance(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
