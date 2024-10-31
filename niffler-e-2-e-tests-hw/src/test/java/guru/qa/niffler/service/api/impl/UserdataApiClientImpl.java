package guru.qa.niffler.service.api.impl;

import guru.qa.niffler.api.UserdataApiClientRetrofit;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.api.UserdataApiClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataApiClientImpl implements UserdataApiClient {

    private final UserdataApiClientRetrofit userdataApiClient = new UserdataApiClientRetrofit();

    @Override
    public UserJson create(UserJson userJson) {
        throw new UnsupportedOperationException("Create user in userdata not supported in Api client");
    }

    @Override
    public Optional<UserJson> findById(UUID id) {
        try {
            return userdataApiClient.findAll().stream().filter(user -> user.getId() == id).findFirst();
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserJson> findByUsername(String username) {
        try {
            return userdataApiClient.findAll("", username).stream().filter(user -> user.getUsername().equals(username)).findFirst();
        } catch (AssertionError ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserJson> findAll() {
        try {
            return userdataApiClient.findAll();
        } catch (AssertionError ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public void sendInvitation(UserJson requester, UserJson addressee) {
        try {
            userdataApiClient.sendInvitation(requester.getUsername(), addressee.getUsername());
        } catch (AssertionError ex) {
            // NOP
        }
    }

    @Override
    public void declineInvitation(UserJson requester, UserJson addressee) {
        try {
            userdataApiClient.declineInvitation(requester.getUsername(), addressee.getUsername());
        } catch (AssertionError ex) {
            // NOP
        }
    }

    @Override
    public void addFriend(UserJson requester, UserJson addressee) {
        try {
            userdataApiClient.sendInvitation(requester.getUsername(), addressee.getUsername());
            userdataApiClient.acceptInvitation(addressee.getUsername(), requester.getUsername());
        } catch (AssertionError ex) {
            // NOP
        }
    }

    @Override
    public void unfriend(UserJson requester, UserJson addressee) {
        try {
            userdataApiClient.removeFriend(requester.getUsername(), addressee.getUsername());
        } catch (AssertionError ex) {
            // NOP
        }
    }

    @Override
    public void remove(UserJson userJson) {
        throw new UnsupportedOperationException("Remove user in userdata not supported in Api client");
    }
}
