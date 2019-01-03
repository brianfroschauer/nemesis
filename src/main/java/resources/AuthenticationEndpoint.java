package resources;

import dao.UserDAO;
import dao.exception.NotAuthorizedException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import model.User;

import util.Credentials;
import util.Key;
import util.Token;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import java.util.Date;
import java.util.Optional;

/**
 * Author: brianfroschauer
 * Date: 14/05/2018
 */
@Path("/auth")
public class AuthenticationEndpoint {

    /**
     * Authenticate the user using the credentials provided.
     *
     * @param credentials to be authenticated.
     * @return the token in the response.
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        final UserDAO userDao = new UserDAO();
        final String username = credentials.getUsername();
        final String password = credentials.getPassword();
        final Optional<User> optionalUser = userDao.authenticateUser(username, password);

        if (optionalUser.isPresent()) {
            final Token token = issueToken(username);
            return Response.ok(token).build();
        }

        throw new NotAuthorizedException("Invalid username or password");
    }

    /**
     * Issue a JWT token for the user.
     *
     * @param username the issued token must be associated to a user.
     * @return the issued token.
     */
    private Token issueToken(String username) {
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Key.getSecret())
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .compact();
        return new Token(token);
    }
}