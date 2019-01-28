package dao.exception;

import util.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Author: brianfroschauer
 * Date: 03/06/2018
 */
public class NotFoundException extends WebApplicationException {

    /**
     * Create a HTTP 404 (Not Found) exception.
     */
    public NotFoundException() {
        super(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Create a HTTP 404 (Not Found) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public NotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage("404", message)).type("application/json").build());
    }
}