package guru.qa.niffler.service.rest;

import guru.qa.niffler.api.AuthUserApiClient;
import guru.qa.niffler.api.FriendApiClient;
import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserRestClient implements UserClient {

    private final FriendApiClient friendApiClient = new FriendApiClient();
    private final AuthUserApiClient authUserApiClient = new AuthUserApiClient();
    private final UserdataApiClient userdataApiClient = new UserdataApiClient();
    private static final String password = "12345";

    @Override
    @Step("Create user using API")
    public UserJson createUser(String username, String password) {
        authUserApiClient.requestRegisterForm();
        authUserApiClient.registerUser(username, password);
        StopWatch sw = new StopWatch();
        sw.start();
        final long limitTime = 3000L;
        UserJson user = null;
        while (sw.getTime(TimeUnit.MILLISECONDS) < limitTime) {
            user = userdataApiClient.getCurrentUser(username);
            if (user != null && user.id() != null) {
               return user.addTestData(new TestData(password));
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("User creation timed out after 3000 milliseconds");
    }

    @Override
    public List<String> createIncomeInvitations(UserJson targetUser, int count) {
        List<String> usernames = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                createUser(username, password);
                friendApiClient.sendInvitation(username, targetUser.username());
                usernames.add(username);
            }
        }
        return usernames;
    }

    @Override
    public List<String> createOutcomeInvitations(UserJson targetUser, int count) {
        List<String> usernames = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                createUser(username, password);
                friendApiClient.sendInvitation(targetUser.username(), username);
                usernames.add(username);
            }
        }
        return usernames;
    }

    @Override
    public void createFriends(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                createUser(username, password);
                friendApiClient.sendInvitation(username, targetUser.username());
                friendApiClient.acceptInvitation(targetUser.username(), username);
            }
        }
    }
}
