package resources;

import dao.StoreDAO;
import dao.exception.ConstraintViolationException;
import dao.exception.DAOException;
import filter.Secured;
import model.Product;
import model.Store;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.ImageWriter;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 02/05/2018
 */
@Path("/stores")
public class StoreResource {

    @Context
    private UriInfo uriInfo;

    public StoreResource() {}

    /**
     * Get store with de specified ID.
     *
     * @param storeId to get from database.
     *
     * @return a store with the specified ID in the response.
     */
    @GET
    @Secured
    @Path("/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStore(@PathParam("storeId") Integer storeId) {
        final StoreDAO storeDao = new StoreDAO();
        final Optional<Store> optionalStore = storeDao.get(Store.class, storeId);

        if (optionalStore.isPresent()) {
            final Store store = optionalStore.get();
            return Response.ok(store).build();
        }

        throw new dao.exception.NotFoundException("Store, " + storeId + ", is not found");
    }

    /**
     * Get all existing stores.
     *
     * @return a list of existing stores in the response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStores() {
        final StoreDAO storeDao = new StoreDAO();
        final List<Store> stores = storeDao.getAll(Store.class);
        return Response.ok(stores).build();
    }

    /**
     * Get a list of products from specified store filtered by category.
     *
     * @param storeId to get from database.
     *
     * @return a list of the store's products in the response.
     */
    @GET
    @Path("/{storeId}/products/{categoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsFromStoreByCategory(@PathParam("storeId") Integer storeId,
                                                   @PathParam("categoryId") Integer categoryId) {
        final StoreDAO storeDao = new StoreDAO();
        final List<Product> products = storeDao.getProductsFromStore(storeId, categoryId);
        return Response.ok(products).build();
    }

    /**
     * Get a list of products from specified store.
     *
     * @param storeId to get from database.
     *
     * @return a list of the store's products in the response.
     */
    @GET
    @Path("/{storeId}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsFromStore(@PathParam("storeId") Integer storeId) {
        final StoreDAO storeDao = new StoreDAO();
        final List<Product> products = storeDao.getProductsFromStore(storeId);
        return Response.ok(products).build();
    }

    /**
     * Search stores by key.
     *
     * @param key to be search.
     *
     * @return a list with matched products.
     */
    @GET
    @Path("/{storeId}/products/{key}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProductsFromStore(@PathParam("storeId") Integer storeId,
                                            @PathParam("key") String key) {
        final StoreDAO storeDao = new StoreDAO();
        final List<Product> products = storeDao.searchProductsFromStore(storeId, key);
        return Response.ok(products).build();
    }

    /**
     * Add a new store in the database.
     *
     * @param store to be created.
     *
     * @return the URI of the new resource in the response.
     */
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStore(@Valid Store store) {
        final StoreDAO storeDao = new StoreDAO();
        try {
            final Integer storeId = storeDao.create(store);
            final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(storeId.toString());
            return Response.created(builder.build()).build();
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Store, " + store.getName() + ", already exists");
        }
    }

    /**
     * Add product to store with the specified ID.
     *
     * @param storeId to get from database.
     * @param productId to be added to the store.
     *
     * @return the created product in the response.
     */
    @POST
    @Secured
    @Path("/{storeId}/products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProductToStore(@PathParam("storeId") Integer storeId, Integer productId) {
        final StoreDAO storeDao = new StoreDAO();
        storeDao.addProductToStore(storeId, productId);
        return Response.ok().build();
    }

    /**
     * Upload a store image.
     *
     * @param storeId to whom the image is uploaded.
     * @param inputStream image to be uploaded.
     *
     * @return a response with status 200 if the image is successfully uploaded.
     */
    @POST
    @Secured
    @Path("/{storeId}/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadStoreImage(@PathParam("storeId") Integer storeId,
                                     @FormDataParam("file") InputStream inputStream) {
        final StoreDAO storeDAO = new StoreDAO();
        final Optional<Store> optionalStore = storeDAO.get(Store.class, storeId);
        if (optionalStore.isPresent()) {
            final Store store = optionalStore.get();
            final String image;
            try {
                image = ImageWriter.uploadImage("stores/" + storeId, inputStream);
            } catch (Exception e) {
                throw new DAOException(e.getMessage());
            }
            store.setImage(image);
            storeDAO.update(store);
            return Response.ok(store).build();
        } else {
            throw new dao.exception.NotFoundException("Store with provided id is not found");
        }
    }

    /**
     * Update the specified store.
     *
     * @param store to be updated.
     *
     * @return the updated store in the response.
     */
    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStore(Store store) {
        final StoreDAO storeDao = new StoreDAO();
        storeDao.update(store);
        return Response.ok(store).build();
    }

    /**
     * Delete store with the specified ID.
     *
     * @param storeId to be deleted.
     *
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{storeId}")
    public Response deleteStore(@PathParam("storeId") Integer storeId) {
        final StoreDAO storeDao = new StoreDAO();
        final Optional<Store> optionalStore = storeDao.get(Store.class, storeId);

        if (optionalStore.isPresent()) {
            final Store store = optionalStore.get();
            storeDao.delete(store);
            return Response.noContent().build();
        }

        throw new NotFoundException("Store, " + storeId + ", is not found");
    }
}