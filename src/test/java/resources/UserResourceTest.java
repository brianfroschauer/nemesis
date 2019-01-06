package resources;

import dao.AbstractDAO;
import dao.ProductDAO;
import dao.StoreDAO;
import dao.UserDAO;

import model.Category;
import model.Product;
import model.Store;
import model.User;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;

public class UserResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(UserResource.class);
    }

    @Test
    public void getUserByUsernameTest() {
        final UserDAO userDAO = new UserDAO();
        final User user = new User(
                "user@mail.com",
                "username",
                "password",
                "name",
                "surname"
        );
        final Integer userId = userDAO.create(user);

        final Response response = target("users/username").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        final User actual = response.readEntity(User.class);
        assertEquals(user, actual);

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
    }

    @Test
    public void getStoresFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        userDAO.addStoreToUser(userId, storeId);

        final Response response = target("users/" + userId + "/stores").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(storeDAO.get(Store.class, storeId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        storeDAO.delete(storeDAO.get(Store.class, storeId).get());
    }

    @Test
    public void getProductsFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        // Add product to user
        userDAO.addProductToUser(userId, productId);

        final Response response = target("users/" + userId + "/products").request(MediaType.APPLICATION_JSON).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();

        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void createUserTest() {
        final UserDAO userDAO = new UserDAO();
        final Map<String, String> data = new HashMap<>();
        data.put("email", "user@mail.com");
        data.put("username", "username");
        data.put("password", "password");
        data.put("name", "name");
        data.put("surname", "surname");

        final Response response = target("users").request().post(Entity.json(data));

        assertThat(userDAO.getUser("username").isPresent()).isTrue();
        final User user = userDAO.getUser("username").get();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("http://localhost:9998/users/" + user.getId(), response.getLocation().toString());

        assertThat(userDAO.get(User.class, user.getId()).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, user.getId()).get());
    }

    @Test
    public void addStoreToUserTest() {
        final UserDAO userDAO = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Map<String, String> storeData = new HashMap<>();
        storeData.put("name", "name");
        storeData.put("description", "description");

        final Response response = target("users/" + userId + "/stores")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(storeData));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        assertThat(storeDAO.getStore("name").isPresent()).isTrue();
        final Store store = storeDAO.getStore("name").get();

        assertEquals("http://localhost:9998/users/" + userId + "/stores/" + store.getId(), response.getLocation().toString());

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(storeDAO.get(Store.class, store.getId()).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        storeDAO.delete(storeDAO.get(Store.class, store.getId()).get());
    }

    @Test
    public void addProductToUserTest() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Category category = new Category("category");
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product(
                "name",
                0,
                1,
                category);

        final Integer productId = productDAO.create(product);

        final Response response = target("users/" + userId + "/products")
                .request()
                .post(Entity.entity(productId, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void updateUserTest() {
        final UserDAO userDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        user.setName("newName");
        user.setUsername("newUsername");

        final Response response = target("users")
                .request()
                .put(Entity.entity(user, MediaType.APPLICATION_JSON));

        final User actual = response.readEntity(User.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(user, actual);

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
    }

    @Test
    public void deleteStoreFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final StoreDAO storeDAO = new StoreDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Store store = new Store("name", "description");
        final Integer storeId = storeDAO.create(store);

        userDAO.addStoreToUser(userId, storeId);

        assertThat(userDAO.get(User.class, userId).get().getStores().isEmpty()).isFalse();

        target("users/" + userId + "/stores/" + storeId).request().delete();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(userDAO.get(User.class, userId).get().getStores().isEmpty()).isTrue();

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(storeDAO.get(Store.class, store.getId()).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        storeDAO.delete(storeDAO.get(Store.class, store.getId()).get());
    }

    @Test
    public void deleteProductFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);

        final Integer productId = productDAO.create(product);

        // Add product to user
        userDAO.addProductToUser(userId, productId);

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        assertThat(userDAO.get(User.class, userId).get().getProducts().isEmpty()).isFalse();

        target("users/" + userId + "/products/" + productId).request().delete();
        assertThat(userDAO.get(User.class, userId).get().getProducts().isEmpty()).isTrue();

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void deleteAllProductsFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final ProductDAO productDAO = new ProductDAO();

        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);

        final Category category = new Category("category");
        final AbstractDAO<Category> categoryDAO = new AbstractDAO<>();
        final Integer categoryId = categoryDAO.create(category);

        final Product product = new Product("name", 0, 1, category);

        final Integer productId = productDAO.create(product);

        // Add product to user
        userDAO.addProductToUser(userId, productId);

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        assertThat(userDAO.get(User.class, userId).get().getProducts().isEmpty()).isFalse();

        target("users/" + userId + "/products").request().delete();
        assertThat(userDAO.get(User.class, userId).get().getProducts().isEmpty()).isTrue();

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void deleteUserTest() {
        final UserDAO userDAO = new UserDAO();
        final User user = new User(
                "email@mail.com",
                "username",
                "password",
                "name",
                "surname");
        final Integer userId = userDAO.create(user);
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        target("users/" + userId).request().delete();
        assertThat(userDAO.get(User.class, userId).isPresent()).isFalse();
    }
}