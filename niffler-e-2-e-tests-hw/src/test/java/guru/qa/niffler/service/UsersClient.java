package guru.qa.niffler.service;

import guru.qa.niffler.model.UserModel;

public interface UsersClient {

    UserModel createUser(UserModel userModel);

    void getIncomeInvitationFromNewUsers(UserModel requester, int count);

    void sendOutcomeInvitationToNewUsers(UserModel requester, int count);

    void addNewFriends(UserModel requester, int count);

    void removeUser(UserModel userModel);

}
