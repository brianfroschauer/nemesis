package resources;

import dao.ProductDAO;
import dao.StoreDAO;
import dao.exception.DAOException;
import model.Product;
import model.Store;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 19/05/2018
 */
@Path("/search")
public class SearchResource {

    /**
     * Search stores by key.
     *
     * @param key to be search.
     * @return a list with matched products.
     */
    @GET
    @Path("/products/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProducts(@PathParam("key") String key) {
        final ProductDAO productDao = new ProductDAO();
        try {
            List<Product> products = productDao.searchProducts(key);
            return Response.ok(products).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Search products by key.
     *
     * @param key to be search.
     * @return a list with matched stores.
     */
    @GET
    @Path("/stores/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStores(@PathParam("key") String key) {
        final StoreDAO storeDao = new StoreDAO();
        try {
            List<Store> stores = storeDao.searchStores(key);
            return Response.ok(stores).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Search stores by key
     *
     * @param key to be search.
     *
     * @return a list with matched products.
     */
    @GET
    @Path("/stores/{storeId}/products/{key}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProductsFromStore(@PathParam("storeId") Integer storeId,
                                            @PathParam("key") String key) {
        final StoreDAO storeDao = new StoreDAO();
        final List<Product> products = storeDao.searchProductsFromStore(storeId, key);
        return Response.ok(products).build();
    }
}