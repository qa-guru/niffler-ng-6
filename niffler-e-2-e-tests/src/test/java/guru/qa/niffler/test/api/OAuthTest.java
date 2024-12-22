package guru.qa.niffler.test.api;

import guru.qa.niffler.api.AuthApiClient;
import org.junit.jupiter.api.Test;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OAuthTest {
    private AuthApiClient authApi = new AuthApiClient();

    private static  String username = "esa";
    private static String password = "12345";

    @Test
    void oauthTest() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final  String token = authApi.getToken(username, password);
        assertNotNull(token);
    }
}
