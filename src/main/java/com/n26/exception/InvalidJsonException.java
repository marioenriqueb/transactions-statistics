package com.n26.exception;

public class InvalidJsonException extends TransactionException{

    public InvalidJsonException(String string) {
	super(string);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
