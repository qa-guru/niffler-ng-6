package guru.qa.niffler.service.rest;

import guru.qa.niffler.api.AuthUserApiClient;
import guru.qa.niffler.api.FriendApiClient;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.utils.RandomDataUtils;

import java.util.ArrayList;
import java.util.List;

public class UserRestClient implements UserClient {

    private final FriendApiClient friendApiClient = new FriendApiClient();
    private final AuthUserApiClient authUserApiClient = new AuthUserApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        return null;
    }

    @Override
    public List<String> createIncomeInvitations(UserJson targetUser, int count) {
        List<String> usernames = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                authUserApiClient.registerUser(/*stub  username*/);
                friendApiClient.sendInvitation(targetUser.username(), username);
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
                authUserApiClient.registerUser(/*stub  username*/);
                friendApiClient.sendInvitation(username, targetUser.username());
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
                authUserApiClient.registerUser(/*stub  username*/);
                friendApiClient.sendInvitation(targetUser.username(), username);
                friendApiClient.acceptInvitation(targetUser.username(), username);
            }
        }
    }
}
