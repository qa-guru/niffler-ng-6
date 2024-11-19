package guru.qa.niffler.utils;

import lombok.SneakyThrows;

import java.net.URI;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class OauthUtils {
    @SneakyThrows
    public static String generateCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code);
        byte[] bytes = verifier.getBytes("US-ASCII");

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @SneakyThrows
    public static String generateCodeChallenge(String codeVerifier) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(codeVerifier.getBytes());
        byte[] digest = md.digest();

        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    public static String getCodeFromRedirectUrl(String redirectUrl) {
        URI uri = URI.create(redirectUrl);
        String queryParam = uri.getQuery();

        String[] params = queryParam.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("code")) {
                return keyValue[1];
            }
        }
        return null;
    }
}
