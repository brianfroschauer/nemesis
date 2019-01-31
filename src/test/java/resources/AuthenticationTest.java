package resources;

import dao.UserDAO;
import model.User;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import util.Credentials;
import util.ErrorMessage;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Author: brianfroschauer
 * Date: 2018-12-29
 */
public class AuthenticationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(AuthenticationEndpoint.class)
                .register(UserResource.class)
                .register(MultiPartFeature.class);
    }

    @Test
    public void validLoginTest() {
        final UserDAO userDAO = new UserDAO();
        final User user = new User(
                "user@mail.com",
                "username",
                "password",
                "name",
                "surname");

        final Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);
        target("users").request().post(userEntity);

        final Credentials credentials = new Credentials("username", "password");
        final Entity<Credentials> credentialsEntity = Entity.entity(credentials, MediaType.APPLICATION_JSON);

        final Response response = target("auth/login").request().post(credentialsEntity);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        assertThat(userDAO.getUserByUsername("username").isPresent()).isTrue();
        userDAO.delete(userDAO.getUserByUsername("username").get());
        assertThat(userDAO.getUserByUsername("username").isPresent()).isFalse();
    }

    @Test
    public void invalidLoginTest() {
        final Credentials credentials = new Credentials("username", "password");
        final Entity<Credentials> credentialsEntity = Entity.entity(credentials, MediaType.APPLICATION_JSON);
        final Response response = target("auth/login").request().post(credentialsEntity);
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        Assert.assertEquals("Invalid username, please try again", response.readEntity(ErrorMessage.class).getMessage());
    }
}
