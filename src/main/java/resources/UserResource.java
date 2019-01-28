package resources;

import dao.StoreDAO;
import dao.UserDAO;
import dao.exception.ConstraintViolationException;
import dao.exception.DAOException;
import dao.exception.NotFoundException;
import filter.Secured;
import model.Product;
import model.Store;
import model.User;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.ImageWriter;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.*;

/**
 * Author: brianfroschauer
 * Date: 02/05/2018
 */
@Path("/users")
public class UserResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Gets user with the specified username.
     *
     * @param username to get from database.
     *
     * @return a user with the specified username in the response.
     */
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) {
        final UserDAO userDao = new UserDAO();
        final Optional<User> optionalUser = userDao.getUserByUsername(username);

        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            return Response.ok(user).build();
        }

        throw new NotFoundException("User, " + username + ", is not found");
    }

    /**
     * Gets a list of products from the specified user.
     *
     * @param userId to get from database.
     * @return a list of user's products in the response.
     */
    @GET
    @Path("/{userId}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsFromUser(@PathParam("userId") Integer userId) {
        final UserDAO userDao = new UserDAO();
        final List<Product> products = userDao.getProductsFromUser(userId);
        return Response.ok(products).build();

    }

    /**
     * Gets a list of stores from the specified user.
     *
     * @param userId to get from database.
     * @return a list of user's stores in the response.
     */
    @GET
    @Path("/{userId}/stores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStoresFromUser(@PathParam("userId") Integer userId) {
        final UserDAO userDao = new UserDAO();
        final List<Store> stores = userDao.getStoresFromUser(userId);
        return Response.ok(stores).build();
    }

    /**
     * Persists a new user in the database.
     *
     * @param user to be persisted.
     *
     * @return the URI of the new resource in the response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid User user) {
        final UserDAO userDao = new UserDAO();
        try {
            final Integer userId = userDao.create(user);
            final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(userId.toString());
            return Response.created(builder.build()).entity(user).build();
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Username or email already exists");
        }
    }

    /**
     * Create a new store and add this to user with the specified ID.
     *
     * @param userId to get from database.
     * @param store to be created.
     *
     * @return the created store in the response.
     */
    @POST
    @Secured
    @Path("/{userId}/stores")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStoreToUser(@PathParam("userId") Integer userId, @Valid Store store) {
        final UserDAO userDao = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();

        try {
            final Integer storeId = storeDAO.create(store);
            userDao.addStoreToUser(userId, storeId);
            final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(storeId.toString());
            return Response.created(builder.build()).entity(store).build();
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Store, " + store.getName() + ", already exists");
        }
    }

    /**
     * Add to the cart the specified product to the user with the specified id.
     *
     * @param userId to get from database.
     * @param productId to be added to the user cart.
     *
     * @return the created product in the response.
     */
    @POST
    @Secured
    @Path("/{userId}/products")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProductToUser(@PathParam("userId") Integer userId, Integer productId) {
        final UserDAO userDao = new UserDAO();
        userDao.addProductToUser(userId, productId);
        return Response.ok().build();
    }

    /**
     * Upload a user image.
     *
     * @param userId to whom the image is uploaded.
     * @param inputStream image to be uploaded.
     *
     * @return a response with status 200 if the image is successfully uploaded.
     */
    @POST
    @Secured
    @Path("/{userId}/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadUserImage(@PathParam("userId") Integer userId,
                                    @FormDataParam("file") InputStream inputStream) {
        final UserDAO userDAO = new UserDAO();
        final Optional<User> optionalUser = userDAO.get(User.class, userId);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final String image;
            try {
                image = ImageWriter.uploadImage("users/" + userId, inputStream);
            } catch (Exception e) {
                throw new DAOException(e.getMessage());
            }
            user.setImage(image);
            userDAO.update(user);
            return Response.ok().build();
        } else {
            throw new NotFoundException("User with provided id is not found");
        }
    }

    /**
     * Update the specified user.
     *
     * @param user to be updated.
     *
     * @return the updated user in the response.
     */
    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@Valid User user) {
        final UserDAO userDao = new UserDAO();
        userDao.update(user);
        return Response.ok(user).build();
    }

    /**
     * Delete the specified store from the user with the specified ID.
     *
     * @param userId who has the store.
     * @param storeId to be deleted.
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{userId}/stores/{storeId}")
    public Response deleteStoreFromUser(@PathParam("userId") Integer userId,
                                        @PathParam("storeId") Integer storeId) {
        final UserDAO userDao = new UserDAO();
        userDao.deleteStoreFromUser(userId, storeId);
        return Response.noContent().build();
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
    @Path("/{userId}/products/{productId}")
    public Response deleteProductFromUser(@PathParam("userId") Integer userId,
                                          @PathParam("productId") Integer productId) {
        final UserDAO userDao = new UserDAO();
        userDao.deleteProductFromUser(userId, productId);
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
    @Path("/{userId}/products")
    public Response deleteAllProductsFromUser(@PathParam("userId") Integer userId) {
        final UserDAO userDao = new UserDAO();
        userDao.deleteAllProductsFromUser(userId);
        return Response.noContent().build();
    }

    /**
     * Delete user with the specified ID.
     *
     * @param userId to be deleted.
     * @return a 204 HTTP status to confirm that the user has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") Integer userId) {
        final UserDAO userDao = new UserDAO();
        final Optional<User> optionalUser = userDao.get(User.class, userId);

        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            userDao.delete(user);
            return Response.noContent().build();
        }

        throw new NotFoundException("User, " + userId + ", is not found");
    }
}