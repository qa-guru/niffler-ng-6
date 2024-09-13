package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.params.provider.Arguments.of;

public class RegisterWebTests {
    private static final Config CFG = Config.getInstance();
    private static final Faker faker = new Faker();
    private static final MainPage mainPage = new MainPage();

    private final String userName = faker.name().username();
    private final String password = faker.internet().password(3, 12);

    @Test
    void checkCreateUser() {
        open(CFG.frontUrl(), LoginPage.class)
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkSuccessfulCreateUser()
                .login(userName, password);
        mainPage.checkSuccessfulLogin();
    }

    @Test
    void checkNotCreateUserWithSimilarUsername() {
        open(CFG.frontUrl(), LoginPage.class)
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkSuccessfulCreateUser()
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkUnsuccessfulCreateUser(userName);
    }

    private static Stream<Arguments> passwordData() {
        return Stream.of(
                of(faker.internet().password(1, 2)),
                of(faker.internet().password(12, 99))

        );
    }

    @ParameterizedTest
    @MethodSource("passwordData")
    void checkNotCreateUserWithWeakOrLongPassword(String password) {
        open(CFG.frontUrl(), LoginPage.class)
                .clickToRegisterPage()
                .registeredUser(userName, password)
                .checkLengthPasswordError();
    }
}
