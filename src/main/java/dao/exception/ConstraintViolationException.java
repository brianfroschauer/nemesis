package dao.exception;

import util.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Author: brianfroschauer
 * Date: 15/05/2018
 */
public class ConstraintViolationException extends WebApplicationException {

    /**
     * Create a HTTP 409 (Conflict) exception.
     */
    public ConstraintViolationException() {
        super(Response.status(Response.Status.CONFLICT).build());
    }

    /**
     * Create a HTTP 409 (Conflict) exception.
     * @param message the String that is the entity of the 409 response.
     */
    public ConstraintViolationException(String message) {
        super(Response.status(Response.Status.CONFLICT).
                entity(new ErrorMessage("409", message)).type("application/json").build());
    }
}