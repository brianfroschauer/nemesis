package resources;

import dao.AbstractDAO;
import dao.ProductDAO;
import dao.UserDAO;
import model.Category;
import model.Product;
import model.User;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.concurrent.Future;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

public class ItemResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(UserResource.class)
                .register(MultiPartFeature.class);
    }

    @Test
    public void getItemsFromUser() {

    }

    @Test
    public void addItemToUser() {
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

        final Product product = new Product("name", 0, 1, category);
        final Integer productId = productDAO.create(product);

        final Form form = new Form();
        form.param("productId", String.valueOf(productId))
            .param("quantity", "1");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/items/" + userId);
        Future<String> response = target.
                request(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_PLAIN)
                .buildPost(Entity.form(form)).submit(String.class);

        // assertEquals(Response.Status.OK.getStatusCode(), response);

        // Delete entities from database
        assertThat(userDAO.get(User.class, userId).isPresent()).isTrue();
        assertThat(productDAO.get(Product.class, productId).isPresent()).isTrue();
        assertThat(categoryDAO.get(Category.class, categoryId).isPresent()).isTrue();
        userDAO.delete(userDAO.get(User.class, userId).get());
        productDAO.delete(productDAO.get(Product.class, productId).get());
        categoryDAO.delete(categoryDAO.get(Category.class, categoryId).get());
    }

    @Test
    public void deleteItemFromUser() {
    }

    @Test
    public void deleteAllItemsFromUser() {
    }
}