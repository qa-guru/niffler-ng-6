package guru.qa.niffler.service.rest;

import guru.qa.niffler.api.AuthUserApiClient;
import guru.qa.niffler.api.FriendApiClient;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserClient;
import guru.qa.niffler.utils.RandomDataUtils;

public class UserRestClient implements UserClient {

    private final FriendApiClient friendApiClient = new FriendApiClient();
    private final AuthUserApiClient authUserApiClient = new AuthUserApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        return null;
    }

    @Override
    public void createIncomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                authUserApiClient.registerUser(/*stub  username*/);
                friendApiClient.sendInvitation(targetUser.username(), username);
            }
        }
    }

    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = RandomDataUtils.randomUsername();
                authUserApiClient.registerUser(/*stub  username*/);
                friendApiClient.sendInvitation(username, targetUser.username());
            }
        }
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
