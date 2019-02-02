package dao.exception;

import util.ErrorMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Author: brianfroschauer
 * Date: 2019-02-02
 */
public class BadRequestException extends WebApplicationException {

    /**
     * Create a HTTP 400 (Bad Request) exception.
     */
    public BadRequestException() {
        super(Response.status(Response.Status.BAD_REQUEST).build());
    }

    /**
     * Create a HTTP 400 (Bad Request) exception.
     * @param message the String that is the entity of the 404 response.
     */
    public BadRequestException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), message)).type("application/json").build());
    }
}
