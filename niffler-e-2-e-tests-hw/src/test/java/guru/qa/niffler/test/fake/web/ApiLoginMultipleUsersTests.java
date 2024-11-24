package guru.qa.niffler.test.fake.web;

import guru.qa.niffler.enums.CookieType;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.ProfilePage;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@WebTest
class ApiLoginMultipleUsersTests {

    @Test
    void multipleUsersAuthWithCreateNewUserExtensionTest(
            @ApiLogin
            @CreateNewUser(
                    currency = CurrencyValues.RUB
            )
            UserJson user1,
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    currency = CurrencyValues.RUB
            )
            UserJson user2
    ) {

        List<HttpCookie> user1Cookies = user1.getTestData().getCookies();
        List<HttpCookie> user2Cookies = user2.getTestData().getCookies();
        String user1Token = user1.getTestData().getToken();
        String user2Token = user2.getTestData().getToken();

        open(ProfilePage.URL, ProfilePage.class)
                .shouldVisiblePageElements()
                .shouldHaveUsername(user2.getUsername());

        assertAll("Assert users AuthData",

                () -> assertAll("Assert user1 cookie and token values not empty",
                        () -> assertNotNull(user1Token),
                        () -> assertFalse(user1Cookies.isEmpty())),

                () -> assertAll("Assert user2 cookie and token values not empty",
                        () -> assertNotNull(user2Token),
                        () -> assertFalse(user2Cookies.isEmpty())),

                () -> assertAll("Assert user1 and user2 cookie and token values not equals",
                        () -> assertNotEquals(user1Token, user2Token),
                        () -> assertNotEquals(
                                user1.getTestData().getCookieValueByName(CookieType.JSESSIONID.getCookieName()),
                                user2.getTestData().getCookieValueByName(CookieType.JSESSIONID.getCookieName())))
        );
    }

    @Test
    void multipleUsersAuthWithoutCreateNewUserExtensionTest(
            @ApiLogin(username = "bee", password = "12345", setupBrowser = true)
            UserJson user1,
            @ApiLogin(username = "duck", password = "12345")
            UserJson user2
    ) {

        open(ProfilePage.URL, ProfilePage.class)
                .shouldVisiblePageElements()
                .shouldHaveUsername(user1.getUsername());

        List<HttpCookie> user1Cookies = user1.getTestData().getCookies(),
                user2Cookies = user2.getTestData().getCookies();
        String user1Token = user1.getTestData().getToken(),
                user2Token = user2.getTestData().getToken();

        assertAll("Assert users AuthData",

                () -> assertAll("Assert user1 cookie and token values not empty",
                        () -> assertNotNull(user1Token),
                        () -> assertFalse(user1Cookies.isEmpty())),

                () -> assertAll("Assert user2 cookie and token values not empty",
                        () -> assertNotNull(user2Token),
                        () -> assertFalse(user2Cookies.isEmpty())),

                () -> assertAll("Assert user1 and user2 cookie and token values not equals",
                        () -> assertNotEquals(user1Token, user2Token),
                        () -> assertNotEquals(
                                user1.getTestData().getCookieValueByName(CookieType.JSESSIONID.getCookieName()),
                                user2.getTestData().getCookieValueByName(CookieType.JSESSIONID.getCookieName())))
        );

    }

}
