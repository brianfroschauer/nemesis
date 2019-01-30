package resources;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.StoreDAO;
import model.Category;
import model.Product;
import model.Store;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

public class SearchResourceTest extends JerseyTest {

    protected Application configure() {
        return new ResourceConfig(SearchResource.class);
    }

    @Test
    public void searchProducts() {
        final ProductDAO productDAO = new ProductDAO();
        final CategoryDAO categoryDAO = new CategoryDAO();

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        final Response response = target("search/products/nam").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(List.class).size() == 1).isTrue();

        // Delete entities from database
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        productDAO.delete(productDAO.get(Product.class, productId).get());

        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());

    }

    @Test
    public void searchStores() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Response response = target("search/stores/nam").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(List.class).size() == 1).isTrue();

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isFalse();
    }

    @Test
    public void searchProductsFromStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final ProductDAO productDAO = new ProductDAO();
        final CategoryDAO categoryDAO = new CategoryDAO();

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        storeDAO.addProductToStore(storeId, productId);

        final Response response = target("search/stores/" + storeId + "/products/nam").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(List.class).size() == 1).isTrue();

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isFalse();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();

        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isFalse();
    }
}