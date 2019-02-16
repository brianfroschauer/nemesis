package resources;

import dao.ItemDAO;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import dao.exception.BadRequestException;
import dao.exception.DAOException;
import filter.Secured;
import model.Item;
import model.Product;
import model.Purchase;
import model.User;
import util.EmailSender;

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

            if (item.getQuantity() > stock) {
                throw new BadRequestException("Quantity, " + item.getQuantity() + " is greater than stock (" + stock + ")");
            } else {
                if (optionalItem.isPresent()) {
                    final Item old = optionalItem.get();
                    product.setStock(stock - item.getQuantity());
                    productDAO.update(product);
                    old.setQuantity(old.getQuantity() + item.getQuantity());
                    itemDAO.update(old);
                    return Response.ok().build();
                } else {
                    itemDAO.addItemToUser(userId, productId, item.getQuantity());
                    product.setStock(stock - item.getQuantity());
                    productDAO.update(product);
                    return Response.ok().build();
                }
            }
        }
        throw new DAOException("Product, " + productId + " is not found");
    }

    /**
     * Delete all products from the user with the specified ID.
     *
     * @param userId who has the store.
     *
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @POST
    @Secured
    @Path("/{userId}/checkout")
    public Response checkout(@PathParam("userId") Integer userId) {
        final ItemDAO itemDAO = new ItemDAO();
        final UserDAO userDAO = new UserDAO();
        final PurchaseDAO purchaseDAO = new PurchaseDAO();
        final Optional<User> optionalUser = userDAO.get(User.class, userId);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final List<Item> items = itemDAO.getItemsFromUser(userId);
            if (!items.isEmpty()) {
                Integer amount = 0;
                for (Item item : items) amount += item.getProduct().getPrice();
                final Purchase purchase = new Purchase(user, items, amount);
                purchaseDAO.create(purchase);
                for (Item item : items) {
                    item.setActive(false);
                    itemDAO.update(item);
                }
                EmailSender.sendPurchaseEmail(purchase.getUser().getEmail(),
                        "Congratulations on your purchase, " + purchase.getUser().getName() + "!", items);
                return Response.ok().build();
            } else throw new BadRequestException("Empty cart");

        } else throw new BadRequestException("User, " + userId + ", is not found");
    }

    /**
     * Delete the specified product from the user with the specified ID.
     *
     * @param itemId to be deleted from user.
     *
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{itemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteItemFromUser(@PathParam("itemId") Integer itemId) {
        final ItemDAO itemDAO = new ItemDAO();
        final ProductDAO productDAO = new ProductDAO();
        final Optional<Item> optionalItem = itemDAO.get(Item.class, itemId);
        if (optionalItem.isPresent()) {
            final Item item = optionalItem.get();
            final Product product = item.getProduct();
            final Integer stock = product.getStock();
            product.setStock(stock + item.getQuantity());
            productDAO.update(product);
            itemDAO.delete(item);
            return Response.noContent().build();
        }
        throw new DAOException("Item, " + itemId + " is not found");
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