package com.n26.exception;

public class NotParseableFieldsException extends TransactionException {

    /**
     * 
     */
    private static final long serialVersionUID = 8301244838628696161L;
    
    public NotParseableFieldsException(String string) {
	super(string);
    }
}
