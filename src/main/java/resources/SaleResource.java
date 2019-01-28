package resources;

import dao.SaleDAO;
import filter.Secured;
import model.Sale;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

/**
 * Author: brianfroschauer
 * Date: 2019-01-07
 */
@Path("/sales")
public class SaleResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Persists a new sale in the database.
     *
     * @param sale to be persisted.
     *
     * @return the URI of the new resource in the response.
     */
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSale(@Valid Sale sale) {
        final SaleDAO saleDAO = new SaleDAO();
        final Integer saleId = saleDAO.create(sale);
        final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(saleId.toString());
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
        final SaleDAO saleDAO = new SaleDAO();
        final List<Sale> sales = saleDAO.getUserPurchases(userId);
        return Response.ok(sales).build();
    }
}