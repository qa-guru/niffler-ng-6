package guru.qa.niffler.service.api.impl;

import guru.qa.niffler.api.AuthApiClientRetrofit;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserdataClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class UsersApiClientImpl implements UsersClient {

    private final AuthApiClientRetrofit apiClient = new AuthApiClientRetrofit();
    private final UserdataClient userdataClient = new UserdataApiClientImpl();

    @Override
    public UserJson createUser(UserJson userJson) {
        return apiClient.register(userJson);
    }

    @Override
    public List<UserJson> getIncomeInvitationFromNewUsers(UserJson to, int count) {
        List<UserJson> incomeInvitations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var addressee = apiClient.register(UserUtils.generateUser());
            userdataClient.findByUsername(addressee.getUsername()).ifPresent(incomeInvitations::add);
            userdataClient.sendInvitation(apiClient.register(UserUtils.generateUser()), to);
        }
        return incomeInvitations;
    }

    @Override
    public List<UserJson> sendOutcomeInvitationToNewUsers(UserJson from, int count) {
        List<UserJson> outcomeInvitations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var addressee = apiClient.register(UserUtils.generateUser());
            userdataClient.findByUsername(addressee.getUsername()).ifPresent(outcomeInvitations::add);
            userdataClient.sendInvitation(from, apiClient.register(UserUtils.generateUser()));
        }
        return outcomeInvitations;
    }

    @Override
    public List<UserJson> addNewFriends(UserJson userJson, int count) {
        List<UserJson> friends = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var addressee = apiClient.register(UserUtils.generateUser());
            userdataClient.findByUsername(addressee.getUsername()).ifPresent(friends::add);
            userdataClient.addFriend(userJson, apiClient.register(UserUtils.generateUser()));
        }
        return friends;
    }

    @Override
    public void removeUser(UserJson userJson) {
        throw new UnsupportedOperationException("Remove user not supported in Api client");
    }

}
