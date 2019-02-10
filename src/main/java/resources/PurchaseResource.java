package resources;

import dao.PurchaseDAO;
import filter.Secured;
import model.Purchase;
import util.EmailSender;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

/**
 * Author: brianfroschauer
 * Date: 2019-01-07
 */
@Path("/purchases")
public class PurchaseResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Persists a new purchase in the database.
     *
     * @param purchase to be persisted.
     *
     * @return the URI of the new resource in the response.
     */
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPurchase(@Valid Purchase purchase) {
        final PurchaseDAO purchaseDAO = new PurchaseDAO();
        final Integer saleId = purchaseDAO.create(purchase);
        final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(saleId.toString());
        EmailSender.sendPurchaseEmail(purchase.getUser().getEmail(),
                "Congratulations on your purchase, " + purchase.getUser().getName() + "!");
        return Response.created(builder.build()).build();
    }

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