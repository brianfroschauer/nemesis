package resources;

import dao.ItemDAO;
import dao.ProductDAO;
import dao.exception.BadRequestException;
import dao.exception.DAOException;
import filter.Secured;
import model.Item;
import model.Product;

import javax.validation.Valid;
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
     * @param item to add to the user cart.
     *
     * @return the created product in the response.
     */
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemToUser(@Valid Item item) {
        final ItemDAO itemDAO = new ItemDAO();
        final ProductDAO productDAO = new ProductDAO();

        final Integer userId = item.getUser().getId();
        final Integer productId = item.getProduct().getId();

        final Optional<Item> optionalItem = itemDAO.getItemFromUser(userId, productId);
        final Optional<Product> optionalProduct = productDAO.get(Product.class, productId);
        final Integer stock;

        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            stock = product.getStock();

            if (optionalItem.isPresent()) {
                final Item old = optionalItem.get();
                final Integer newQuantity = old.getQuantity() + item.getQuantity();
                if (newQuantity > stock) {
                    throw new BadRequestException("Quantity, " + newQuantity + " is greater than stock (" + stock + ")");
                }
                product.setStock(stock - item.getQuantity());
                old.setQuantity(newQuantity);
                itemDAO.update(old);
                return Response.ok(old).build();
            } else {
                itemDAO.addItemToUser(userId, productId, item.getQuantity());
                product.setStock(stock - item.getQuantity());
                return Response.ok().build();
            }
        }
        throw new DAOException("Product, " + productId + "is not found");
    }

    /**
     * Delete the specified product from the user with the specified ID.
     *
     * @param item to be deleted from user.
     *
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteItemFromUser(Item item) {
        final ItemDAO itemDAO = new ItemDAO();
        itemDAO.deleteItemFromUser(item.getUser().getId(), item.getProduct().getId());
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
    @Path("/{userId}")
    public Response deleteAllItemsFromUser(@PathParam("userId") Integer userId) {
        final ItemDAO itemDAO = new ItemDAO();
        itemDAO.deleteAllItemsFromUser(userId);
        return Response.noContent().build();
    }
}