package com.n26.exception;

public class TransactionOlderException extends TransactionException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TransactionOlderException(String string) {
	super(string);
    }
}
