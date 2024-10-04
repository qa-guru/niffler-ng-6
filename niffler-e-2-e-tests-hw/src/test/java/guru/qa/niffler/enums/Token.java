package guru.qa.niffler.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Token {

    CSRF("XSRF-TOKEN"), JSESSIONID("JSESSIONID");

    private final String cookieName;

    public static Optional<Token> getEnumByCookieName(String cookieName) {
        return Arrays.stream(values()).filter(token -> token.getCookieName().equalsIgnoreCase(cookieName)).findFirst();
    }

}
