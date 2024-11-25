package guru.qa.niffler.service.api.impl;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UserdataClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.UserUtils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class UsersApiClientImpl implements UsersClient {

    private final AuthApiClient apiClient = new AuthApiClient();
    private final UserdataClient userdataClient = new UserdataApiClientImpl();

    @Override
    public @Nonnull UserJson createUser(UserJson userJson) {
        return apiClient.register(userJson.getUsername(), userJson.getPassword());
    }

    @Override
    public @Nonnull List<UserJson> getIncomeInvitationFromNewUsers(UserJson to, int count) {
        List<UserJson> incomeInvitations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var from = UserUtils.generateUser();
            from = apiClient.register(from.getUsername(), from.getPassword());
            incomeInvitations.add(from);
            userdataClient.sendInvitation(from, to);
        }
        return incomeInvitations;
    }

    @Override
    public @Nonnull List<UserJson> sendOutcomeInvitationToNewUsers(UserJson from, int count) {
        List<UserJson> outcomeInvitations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var to = UserUtils.generateUser();
            to = apiClient.register(to.getUsername(), to.getPassword());
            outcomeInvitations.add(to);
            userdataClient.sendInvitation(from, to);
        }
        return outcomeInvitations;
    }

    @Override
    public @Nonnull List<UserJson> addNewFriends(UserJson from, int count) {
        List<UserJson> friends = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var to = UserUtils.generateUser();
            to = apiClient.register(to.getUsername(), to.getPassword());
            friends.add(to);
            userdataClient.addFriend(from, to);
        }
        return friends;
    }

    @Override
    public void removeUser(UserJson userJson) {
        throw new UnsupportedOperationException("Remove user not supported in Api client");
    }

    @Override
    public void removeAllUsers() {
        throw new UnsupportedOperationException("Remove all users not supported in Api client");
    }

}
