package resources;

import dao.PurchaseDAO;
import filter.Secured;
import model.Purchase;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2019-01-07
 */
@Path("/purchases")
public class PurchaseResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Gets a list of sales from the specified user.
     *
     * @param userId to get from database.
     * @return a list of user's sales in the response.
     */
    @GET
    @Secured
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPurchases(@PathParam("userId") Integer userId) {
        final PurchaseDAO purchaseDAO = new PurchaseDAO();
        final List<Purchase> purchases = purchaseDAO.getUserPurchases(userId);
        return Response.ok(purchases).build();
    }
}