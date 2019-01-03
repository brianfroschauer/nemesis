package resources;

import dao.StoreDAO;
import dao.exception.ConstraintViolationException;
import dao.exception.DAOException;
import filter.Secured;
import model.Product;
import model.Store;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.FileManager;
import util.Image;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 02/05/2018
 */
@Path("/stores")
public class StoreResource {

    public StoreResource() {}

    /**
     * Get store with de specified ID.
     *
     * @param storeId to get from database.
     * @return a store with the specified ID in the response.
     */
    @GET
    @Secured
    @Path("/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStore(@PathParam("storeId") Integer storeId) {
        StoreDAO storeDao = new StoreDAO();
        try {
            Store store = storeDao.get(Store.class, storeId).get();
            return Response.ok(store).build();
        } catch (NoResultException e) {
            throw new NotFoundException("Store, " + storeId + ", is not found");
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Get all existing stores.
     *
     * @return a list of existing stores in the response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStores() {
        StoreDAO storeDao = new StoreDAO();
        try {
            List<Store> stores = storeDao.getAll(Store.class);
            return Response.ok(stores).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Get a list of products from specified store filtered by category.
     *
     * @param storeId to get from database.
     * @return a list of the store's products in the response.
     */
    @GET
    @Path("/{storeId}/products/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsFromStore(@PathParam("storeId") Integer storeId,
                                         @PathParam("category") String category) {
        StoreDAO storeDao = new StoreDAO();
        try {
            List<Product> products = storeDao.getProductsFromStore(storeId, 1); // ojo aca
            return Response.ok(products).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Get a list of products from specified store.
     *
     * @param storeId to get from database.
     * @return a list of the store's products in the response.
     */
    @GET
    @Path("/{storeId}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsFromStore(@PathParam("storeId") Integer storeId) {
        StoreDAO storeDao = new StoreDAO();
        try {
            List<Product> products = storeDao.getProductsFromStore(storeId);
            return Response.ok(products).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Search stores by key.
     *
     * @param key to be search.
     * @return a list with matched products.
     */
    @GET
    @Path("/{storeId}/search/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProductsFromStore(@PathParam("storeId") Integer storeId,
                                            @PathParam("key") String key) {
        StoreDAO storeDao = new StoreDAO();
        try {
            List<Product> products = storeDao.searchProductsFromStore(storeId, key);
            return Response.ok(products).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Get image from store.
     *
     * @param storeId to identify the image.
     * @return the image in the response.
     */
    @GET
    @Path("/image/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStoreImage(@PathParam("storeId") Integer storeId) {
        try {
            File file = new File("/Users/brianfroschauer/Projects/nemesis/images/stores/" + storeId + ".jpg");
            if (file.exists()) {
                String encodedFile = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));
                Image image = new Image(encodedFile);
                return Response.ok(image).build();
            } else {
                File defaultFile = new File("/Users/brianfroschauer/Projects/nemesis/images/default/store.png");
                String encodedFile = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(defaultFile.toPath()));
                Image image = new Image(encodedFile);
                return Response.ok(image).build();
            }
        } catch (RuntimeException | IOException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Add a new store in the database.
     *
     * @param store to be created.
     * @return the created store in the response.
     */
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStore(@Valid Store store) {
        StoreDAO storeDao = new StoreDAO();
        try {
            Integer storeId = storeDao.create(store);
            return Response.created(URI.create("/stores/" + storeId)).build();
            // return Response.status(Response.Status.CREATED).entity(store).build();
        } catch (PersistenceException e) {
            throw new ConstraintViolationException("Store, " + store.getName() + ", already exists");
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Add product to store with the specified ID.
     *
     * @param storeId to get from database.
     * @param product to be created.
     * @return the created product in the response.
     */
    @POST
    @Secured
    @Path("/{storeId}/products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProductToStore(@PathParam("storeId") Integer storeId, Product product) {
        StoreDAO storeDao = new StoreDAO();
        try {
            storeDao.addProductToStore(storeId, product.getId()); // ojo aca
            return Response.ok(product).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Upload store image.
     *
     * @param storeId to get from database.
     * @return the updated user in the response.
     */
    @POST
    @Secured
    @Path("/images/{storeId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadStoreImage(@PathParam("storeId") Integer storeId,
                                    @FormDataParam("file") InputStream inputStream) {
        try {
            String fileName = "/" + storeId.toString() + ".jpg";
            String filePath = "/stores" + fileName;
            FileManager.writeImage(inputStream, filePath);
            return Response.ok("Image uploaded successfully").build();
        } catch (RuntimeException | IOException e) {
            throw new DAOException();
        }
    }

    /**
     * Update the specified store.
     *
     * @param store to be updated.
     * @return the updated store in the response.
     */
    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStore(Store store) {
        StoreDAO storeDao = new StoreDAO();
        try {
            storeDao.update(store);
            return Response.ok(store).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Delete the specified product from the store with the specified ID.
     *
     * @param storeId who has the store.
     * @param productId to be deleted.
     * @return a 204 HTTP status to confirm that the product has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{storeId}/products/{productId}")
    public Response deleteProductFromStore(@PathParam("storeId") Integer storeId,
                                           @PathParam("productId") Integer productId) {
        StoreDAO storeDao = new StoreDAO();
        try {
            //storeDao.deleteProductFromStore(storeId, productId);
            return Response.noContent().build();
        } catch (NullPointerException e) {
            throw new NotFoundException("Store, " + storeId + " or Product, " + productId + ", is not found");
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Delete store with the specified ID.
     *
     * @param storeId to be deleted.
     * @return a 204 HTTP status to confirm that the store has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{storeId}")
    public Response deleteStore(@PathParam("storeId") Integer storeId) {
        StoreDAO storeDao = new StoreDAO();
        try {
            //storeDao.delete(Store.class, storeId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Store, " + storeId + ", is not found");
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }
}