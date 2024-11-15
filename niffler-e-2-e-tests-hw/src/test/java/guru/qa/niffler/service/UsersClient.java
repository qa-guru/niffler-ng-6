package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.UserJson;

import java.util.List;

public interface UsersClient {

    UserJson createUser(UserJson userJson);

    List<UserJson> getIncomeInvitationFromNewUsers(UserJson to, int count);

    List<UserJson> sendOutcomeInvitationToNewUsers(UserJson from, int count);

    List<UserJson> addNewFriends(UserJson userJson, int count);

    void removeUser(UserJson userJson);

    void removeAllUsers();

}
