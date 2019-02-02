package resources;

import dao.ItemDAO;
import dao.ProductDAO;
import dao.exception.BadRequestException;
import dao.exception.DAOException;
import filter.Secured;
import model.Item;
import model.Product;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 2019-02-02
 */
@Path("/items")
public class ItemResource {

    /**
     * Gets a list of products from the specified user.
     *
     * @param userId to get from database.
     * @return a list of user's products in the response.
     */
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemsFromUser(@PathParam("userId") Integer userId) {
        final ItemDAO itemDAO = new ItemDAO();
        final List<Item> items = itemDAO.getItemsFromUser(userId);
        return Response.ok(items).build();

    }

    /**
     * Add to the cart the specified product to the user with the specified id.
     *
     * @param userId to get from database.
     * @param productId to be added to the user cart.
     * @return the created product in the response.
     */
    @POST
    @Secured
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemToUser(@PathParam("userId") Integer userId,
                                  @FormParam("productId") Integer productId,
                                  @FormParam("quantity") Integer quantity) {
        final ItemDAO itemDAO = new ItemDAO();
        final ProductDAO productDAO = new ProductDAO();
        final Optional<Item> optionalItem = itemDAO.getItemFromUser(userId, productId);
        final Optional<Product> optionalProduct = productDAO.get(Product.class, productId);
        final Integer stock;
        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            stock = product.getStock();

            if (optionalItem.isPresent()) {
                final Item item = optionalItem.get();
                final Integer newQuantity = item.getQuantity() + quantity;
                if (newQuantity > stock)
                    throw new BadRequestException("Quantity, " + quantity + " is greater than stock (" + stock + ")");
                item.setQuantity(newQuantity);
                itemDAO.update(item);
                return Response.ok(item).build();
            } else {
                itemDAO.addItemToUser(userId, productId, quantity);
                return Response.ok().build();
            }
        }
        throw new DAOException("Product, " + productId + "is not found");
    }

    /**
     * Delete the specified product from the user with the specified ID.
     *
     * @param userId who has the store.
     * @param productId to be deleted.
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteItemFromUser(@PathParam("userId") Integer userId,
                                       @FormParam("productId") Integer productId) {
        final ItemDAO itemDAO = new ItemDAO();
        itemDAO.deleteItemFromUser(userId, productId);
        return Response.noContent().build();

    }

    /**
     * Delete all products from the user with the specified ID.
     *
     * @param userId who has the store.
     *
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{userId}/all")
    public Response deleteAllItemsFromUser(@PathParam("userId") Integer userId) {
        final ItemDAO itemDAO = new ItemDAO();
        itemDAO.deleteAllItemsFromUser(userId);
        return Response.noContent().build();
    }
}
