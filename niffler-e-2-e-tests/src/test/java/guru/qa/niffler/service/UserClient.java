package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;

public interface UserClient {
    UserJson createUser(String username, String password);

    List<String> createIncomeInvitations(UserJson targetUser, int count);

    List<String> createOutcomeInvitations(UserJson targetUser, int count);

    void createFriends(UserJson targetUser, int count);
}
