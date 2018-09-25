package com.n26.web.rest.error;

import java.io.Serializable;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
	return status;
    }
}
