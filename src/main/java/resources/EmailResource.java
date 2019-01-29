package resources;

import util.EmailSender;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Author: brianfroschauer
 * Date: 2019-01-29
 */
@Path("/email")
public class EmailResource {

    @Context
    private UriInfo uriInfo;

    @GET
    public Response sendEmail() {
        EmailSender.send("brian.froschauer@ing.austral.edu.ar", "Test email", "HOLA!!!");
        return Response.noContent().build();
    }

}
