package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.AuthUserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class UserCreateInBdTest {
    @Test
    void createUserInDb() {
        UserJson user = new AuthUserDbClient().createUser(new UserJson(
                null,
                RandomDataUtils.randomUsername(),
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }

    @Test
    void createUserSpringInDb() {
        AuthUserDbClient authUserDbClient = new AuthUserDbClient();
        UserJson user = authUserDbClient.createUserSpring(new UserJson(
                null,
                RandomDataUtils.randomUsername(),
                null,
                null,
                RandomDataUtils.randomName(),
                CurrencyValues.RUB,
                null,
                null
        ));
        System.out.println(user);
    }
}
