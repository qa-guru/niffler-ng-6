package guru.qa.niffler.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class OauthUtils {

    // Метод для генерации code_verifier
    public static String generateCodeVerifier() {
        // Создаем массив случайных байтов
        byte[] randomBytes = new byte[32];
        new Random().nextBytes(randomBytes);

        // Преобразуем байты в строку с использованием Base64 (URL и безопасно для файловой системы)
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    // Метод для генерации code_challenge
    public static String generateCodeChallenge(String codeVerifier) {
        try {
            // Создаем хэш SHA-256 от code_verifier
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));

            // Преобразуем хэш в строку Base64 (URL и безопасно для файловой системы)
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}