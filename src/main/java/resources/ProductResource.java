package resources;

import dao.ProductDAO;
import dao.exception.DAOException;
import filter.Secured;
import model.Product;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.FileManager;
import util.Image;

import javax.persistence.NoResultException;
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
 * Date: 01/05/2018
 */
@Path("/products")
public class ProductResource {

    /**
     * Get product with de specified ID.
     *
     * @param productId to get from database.
     * @return a product with the specified ID in the response.
     */
    @GET
    @Secured
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productId") Integer productId) {
        ProductDAO productDao = new ProductDAO();
        try {
            Product product = productDao.get(Product.class, productId).get();
            return Response.ok(product).build();
        } catch (NoResultException e) {
            throw new NotFoundException("Product, " + productId + ", is not found");
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Get all existing products.
     *
     * @return a list of existing products in the response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        ProductDAO productDao = new ProductDAO();
        try {
            List<Product> products = productDao.getAll(Product.class);
            return Response.ok(products).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Get image from product.
     *
     * @param productId to identify the image.
     * @return the image in the response.
     */
    @GET
    @Path("/image/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductImage(@PathParam("productId") Integer productId) {
        try {
            File file = new File("/Users/brianfroschauer/Projects/nemesis/images/products/" + productId + ".jpg");
            if (file.exists()) {
                String encodedFile = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));
                Image image = new Image(encodedFile);
                return Response.ok(image).build();
            } else {
                File defaultFile = new File("/Users/brianfroschauer/Projects/nemesis/images/default/product.png");
                String encodedFile = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(defaultFile.toPath()));
                Image image = new Image(encodedFile);
                return Response.ok(image).build();
            }
        } catch (RuntimeException | IOException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Add a new product in the database.
     *
     * @param product to be created.
     * @return the created product in the response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(Product product) {
        ProductDAO productDao = new ProductDAO();
        try {
            Integer id = productDao.create(product);
            return Response.created(URI.create("/products/" + id)).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Upload product image.
     *
     * @param productId to get from database.
     * @return the updated user in the response.
     */
    @POST
    @Secured
    @Path("/images/{productId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadProductImage(@PathParam("productId") Integer productId,
                                    @FormDataParam("file") InputStream inputStream) {
        try {
            String fileName = "/" + productId.toString() + ".jpg";
            String filePath = "/products" + fileName;
            FileManager.writeImage(inputStream, filePath);
            return Response.ok("Image uploaded successfully").build();
        } catch (RuntimeException | IOException e) {
            throw new DAOException();
        }
    }

    /**
     * Update the specified product.
     *
     * @param product to be updated.
     * @return the updated product in the response.
     */
    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(Product product) {
        ProductDAO productDao = new ProductDAO();
        try {
            productDao.update(product);
            return Response.ok(product).build();
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }

    /**
     * Delete product with the specified ID.
     *
     * @param productId to be deleted.
     * @return a 204 HTTP status to confirm that the product has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{productId}")
    public Response deleteProduct(@PathParam("productId") Integer productId) {
        ProductDAO productDao = new ProductDAO();
        try {
            //productDao.delete(Product.class, productId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Product, " + productId + ", is not found");
        } catch (RuntimeException e) {
            throw new DAOException();
        }
    }
}