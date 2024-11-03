package guru.qa.niffler.service.rest;

import guru.qa.niffler.api.AuthUserApiClient;
import guru.qa.niffler.api.FriendApiClient;
import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserRestClient implements UserClient {

    private final FriendApiClient friendApiClient = new FriendApiClient();
    private final AuthUserApiClient authUserApiClient = new AuthUserApiClient();
    private final UserdataApiClient userdataApiClient = new UserdataApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        authUserApiClient.requestRegisterForm();
        authUserApiClient.registerUser(username, password);
        StopWatch sw = new StopWatch();
        sw.start();
        UserJson user = null;
        while (sw.getTime(TimeUnit.MILLISECONDS) < 3000) {
            user = userdataApiClient.getCurrentUser(username);
            if (user != null && user.id() != null) {
                user.addTestData(new TestData(password, null, null));
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return user;
    }

    @Override
    public List<String> createIncomeInvitations(UserJson targetUser, int count) {
        List<String> usernames = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                createUser(username, "12345");
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
                createUser(username, "12345");
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
                createUser(username, "12345");
                friendApiClient.sendInvitation(username, targetUser.username());
                friendApiClient.acceptInvitation(targetUser.username(), username);
            }
        }
    }
}
