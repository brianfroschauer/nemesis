package dao.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Author: brianfroschauer
 * Date: 15/05/2018
 */
public class DAOException extends WebApplicationException {

    /**
     * Create a HTTP 500 (Internal Server Error) exception.
     */
    public DAOException() {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Create a HTTP 500 (Internal Server Error) exception.
     * @param message the String that is the entity of the 500 response.
     */
    public DAOException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(message).type("text/plain").build());
    }
}
