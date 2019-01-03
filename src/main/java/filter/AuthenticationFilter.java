package filter;

import dao.exception.NotAuthorizedException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import util.Key;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

/**
 * Author: brianfroschauer
 * Date: 11/04/2018
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws NotAuthorizedException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            validateToken(token);
        } catch (JwtException e) {
            throw new NotAuthorizedException("Your session has expired, please log in again");
        }
    }

    /**
     * Check if it was issued by the server and if it's not expired.
     *
     * @param token to be validated.
     * @throws JwtException if the token is invalid.
     */
    private void validateToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(Key.getSecret())).parseClaimsJws(token);
    }
}