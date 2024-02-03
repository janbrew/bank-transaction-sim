package net.jsf.exceptions;

import java.util.NoSuchElementException;

public class AccountNotFound extends NoSuchElementException {
    public AccountNotFound(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
