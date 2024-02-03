package net.jsf.exceptions;

public class DepositLimitReached extends Exception {
    public DepositLimitReached(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }    
}
