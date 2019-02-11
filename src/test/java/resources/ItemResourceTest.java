package resources;

import dao.AbstractDAO;
import dao.ItemDAO;
import dao.ProductDAO;
import dao.UserDAO;
import model.Category;
import model.Item;
import model.Product;
import model.User;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ItemResource.class)
                .register(MultiPartFeature.class);
    }

    @Test
    public void getItemsFromUser() {
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();
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

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        itemDAO.addItemToUser(userId, productId, 1);

        final Response response = target("/items/" + userId)
                .request()
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(1, response.readEntity(List.class).size());

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();

        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());

        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isTrue();
    }

    @Test
    public void addItemToUser() {
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();
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

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        final Item item = new Item(user, product, 1);

        final Response response = target("/items")
                .request()
                .post(Entity.entity(item, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Delete entities from database
        itemDAO.deleteAllItemsFromUser(userId);
        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isTrue();
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void deleteAllItemsFromUserTest() {
        final UserDAO userDAO = new UserDAO();
        final ItemDAO itemDAO = new ItemDAO();
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

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        itemDAO.addItemToUser(userId, productId, 1);

        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isFalse();

        target("/items/" + userId + "/all").request().delete();

        assertThat(itemDAO.getItemsFromUser(userId).isEmpty()).isTrue();

        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }
}