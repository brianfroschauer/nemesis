package dao.exception;

import util.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Author: brianfroschauer
 * Date: 18/05/2018
 */
public class NotAuthorizedException extends WebApplicationException {

    /**
     * Create a HTTP 401 (Unauthorized) exception.
     */
    public NotAuthorizedException() {
        super(Response.status(Response.Status.UNAUTHORIZED).build());
    }

    /**
     * Create a HTTP 401 (Unauthorized) exception.
     * @param message the String that is the entity of the 401 response.
     */
    public NotAuthorizedException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).
                entity(new ErrorMessage("401", message)).type("application/json").build());
    }
}
