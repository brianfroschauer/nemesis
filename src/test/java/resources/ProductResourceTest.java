package resources;

import dao.AbstractDAO;
import dao.ProductDAO;
import model.Category;
import model.Product;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;

public class ProductResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ProductResource.class);
    }

    @Test
    public void getProduct() {
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        final Response response = target("products/" + productId.toString()).request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final Product actual = response.readEntity(Product.class);
        assertEquals(product, actual);

        // Delete entities from database
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        productDAO.delete(productDAO.get(Product.class, productId).get());

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void getAllProducts() {
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        final Response response = target("products").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(List.class).size() == 1).isTrue();

        // Delete entities from database
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        productDAO.delete(productDAO.get(Product.class, productId).get());

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void createProduct() {
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);

        final Response response = target("products")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(product, MediaType.APPLICATION_JSON));

        assertThat(productDAO.getAll(Product.class).size() == 1).isTrue();
        final Product actual = productDAO.getAll(Product.class).get(0);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("http://localhost:9998/products/" + actual.getId(), response.getLocation().toString());

        assertThat(productDAO.get(Product.class, actual.getId()).isPresent()).isTrue();
        productDAO.delete(productDAO.get(Product.class, actual.getId()).get());

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());

    }

    @Test
    public void updateProduct() {
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        product.setName("newName");

        final Response response = target("products")
                .request()
                .put(Entity.entity(product, MediaType.APPLICATION_JSON));

        final Product actual = response.readEntity(Product.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(product, actual);

        // Delete entities from database
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        productDAO.delete(productDAO.get(Product.class, productId).get());

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void deleteProduct() {
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        target("products/" + productId).request().delete();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }
}