package resources;

import dao.ProductDAO;
import dao.exception.DAOException;
import filter.Secured;
import model.Product;
import org.glassfish.jersey.media.multipart.FormDataParam;
import util.ImageWriter;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 01/05/2018
 */
@Path("/products")
public class ProductResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Get product with de specified ID.
     *
     * @param productId to get from database.
     *
     * @return a product with the specified ID in the response.
     */
    @GET
    @Secured
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productId") Integer productId) {
        final ProductDAO productDao = new ProductDAO();
        final Optional<Product> optionalProduct = productDao.get(Product.class, productId);

        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            return Response.ok(product).build();
        }

        throw new dao.exception.NotFoundException("Product, " + productId + ", is not found");
    }

    /**
     * Get all existing products.
     *
     * @return a list of existing products in the response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        final ProductDAO productDao = new ProductDAO();
        final List<Product> products = productDao.getAll(Product.class);
        return Response.ok(products).build();
    }

    /**
     * Add a new product in the database.
     *
     * @param product to be created.
     *
     * @return the created product in the response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(Product product) {
        final ProductDAO productDao = new ProductDAO();
        final Integer productId = productDao.create(product);
        final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(productId.toString());
        return Response.created(builder.build()).build();
    }

    /**
     * Upload a product image.
     *
     * @param productId to whom the image is uploaded.
     * @param inputStream image to be uploaded.
     *
     * @return a response with status 200 if the image is successfully uploaded.
     */
    @POST
    @Secured
    @Path("/{productId}/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProductImage(@PathParam("productId") Integer productId,
                                       @FormDataParam("file") InputStream inputStream) {
        final ProductDAO productDAO = new ProductDAO();
        final Optional<Product> optionalProduct = productDAO.get(Product.class, productId);
        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            final String image;
            try {
                image = ImageWriter.uploadImage("products/" + productId, inputStream);
            } catch (Exception e) {
                throw new DAOException(e.getMessage());
            }
            product.setImage(image);
            productDAO.update(product);
            return Response.ok(product).build();
        } else {
            throw new dao.exception.NotFoundException("Product with provided id is not found");
        }
    }

    /**
     * Update the specified product.
     *
     * @param product to be updated.
     *
     * @return the updated product in the response.
     */
    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(Product product) {
        final ProductDAO productDao = new ProductDAO();
        productDao.update(product);
        return Response.ok(product).build();
    }

    /**
     * Delete product with the specified ID.
     *
     * @param productId to be deleted.
     *
     * @return a 204 HTTP status to confirm that the product has been deleted successfully.
     */
    @DELETE
    @Secured
    @Path("/{productId}")
    public Response deleteProduct(@PathParam("productId") Integer productId) {
        final ProductDAO productDao = new ProductDAO();
        final Optional<Product> optionalProduct = productDao.get(Product.class, productId);

        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            productDao.delete(product);
            return Response.noContent().build();
        }

        throw new NotFoundException("Product, " + productDao + ", is not found");
    }
}