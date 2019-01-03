package resources;

import dao.UserDAO;
import model.User;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import util.Credentials;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Author: brianfroschauer
 * Date: 2018-12-29
 */
public class AuthenticationTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(AuthenticationEndpoint.class);
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
        userDAO.create(user);
        final Credentials credentials = new Credentials("username", "password");
        final Entity<Credentials> credentialsEntity = Entity.entity(credentials, MediaType.APPLICATION_JSON);
        final Response response = target("auth/login").request().post(credentialsEntity);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        userDAO.delete(user);
    }

    @Test
    public void invalidLoginTest() {
        final Credentials credentials = new Credentials("username", "password");
        final Entity<Credentials> credentialsEntity = Entity.entity(credentials, MediaType.APPLICATION_JSON);
        final Response response = target("auth/login").request().post(credentialsEntity);
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        Assert.assertEquals("Invalid username or password", response.readEntity(String.class));
    }
}
