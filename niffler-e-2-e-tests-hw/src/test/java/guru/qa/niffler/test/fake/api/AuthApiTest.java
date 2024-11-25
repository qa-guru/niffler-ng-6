package guru.qa.niffler.test.fake.api;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class AuthApiTest {

    AuthApiClient authClient = new AuthApiClient();
    UserJson user = UserUtils.generateUser();

    @Test
    void authTest() {
        authClient.register(user.getUsername(), user.getPassword());
        var token = authClient.signIn(user.getUsername(), user.getPassword());
        log.info("Token: {}", token);
        Assertions.assertNotNull(token);
    }

}
