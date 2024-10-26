package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;

public interface UsersClient {
    UserJson createUser(String username, String password);

    List<String> addIncomeInvitation(UserJson targetUser, int count);

    List<String> addOutcomeInvitation(UserJson targetUser, int count);

    List<String> addFriend(UserJson targetUser, int count);
}
