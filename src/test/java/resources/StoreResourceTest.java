package resources;

import dao.AbstractDAO;
import dao.ProductDAO;
import dao.StoreDAO;
import model.Category;
import model.Product;
import model.Store;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

public class StoreResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(StoreResource.class);
    }

    @Test
    public void getStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Response response = target("stores/" + storeId.toString()).request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final Store actual = response.readEntity(Store.class);
        assertEquals(store, actual);

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
    }

    @Test
    public void getAllStores() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Response response = target("stores").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertThat(response.readEntity(List.class).size() == 1).isTrue();

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
    }

    @Test
    public void getProductsFromStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final ProductDAO productDAO = new ProductDAO();

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        // Add product to store
        storeDAO.addProductToStore(storeId, productId);

        final Response response = target("stores/" + storeId + "/products").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();

        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void getProductsFromStoreByCategory() {
        final StoreDAO storeDAO = new StoreDAO();
        final ProductDAO productDAO = new ProductDAO();

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        // Add product to store
        storeDAO.addProductToStore(storeId, productId);

        final Response response = target("stores/" + storeId + "/products/" + categoryId).request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();

        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void searchProductsFromStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final ProductDAO productDAO = new ProductDAO();

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        // Add product to store
        storeDAO.addProductToStore(storeId, productId);

        final Response response = target("stores/" + storeId + "/products/nam").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();

        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
        assertThat(productDAO.get(Product.class, productId).isPresent()).isFalse();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void createStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final Map<String, String> data = new HashMap<>();
        data.put("name", "name");
        data.put("description", "description");

        final Response response = target("stores").request().post(Entity.json(data));

        assertThat(storeDAO.getStore("name").isPresent()).isTrue();
        final Store store = storeDAO.getStore("name").get();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("http://localhost:9998/stores/" + store.getId(), response.getLocation().toString());

        assertThat(storeDAO.get(Store.class, store.getId()).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, store.getId()).get());
    }

    @Test
    public void addProductToStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        final Response response = target("stores/" + storeId + "/products")
                .request()
                .post(Entity.entity(productId, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

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

    @Test
    public void updateStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        store.setName("newName");

        final Response response = target("stores")
                .request()
                .put(Entity.entity(store, MediaType.APPLICATION_JSON));

        final Store actual = response.readEntity(Store.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(store, actual);

        // Delete entities from database
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
    }

    @Test
    public void deleteStore() {
        final StoreDAO storeDAO = new StoreDAO();
        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        target("stores/" + storeId).request().delete();
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isFalse();
    }
}