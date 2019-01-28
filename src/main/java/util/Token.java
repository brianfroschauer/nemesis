package util;

/**
 * Author: brianfroschauer
 * Date: 14/05/2018
 */
public class Token {

    private final String token;
    private static final String type = "Bearer";
    private final int expiration;

    public Token(String token, int expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public int getExpiration() {
        return expiration;
    }
}
