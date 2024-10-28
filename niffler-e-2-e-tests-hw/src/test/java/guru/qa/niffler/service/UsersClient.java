package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public interface UsersClient {

    UserJson createUser(UserJson userJson);

    void getIncomeInvitationFromNewUsers(UserJson requester, int count);

    void sendOutcomeInvitationToNewUsers(UserJson requester, int count);

    void addNewFriends(UserJson requester, int count);

    void removeUser(UserJson userJson);

}
