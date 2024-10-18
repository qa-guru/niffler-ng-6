package guru.qa.niffler.service;

import guru.qa.niffler.model.UserModel;

public interface UsersDbClient {

    UserModel createUserInAuthAndUserdataDBs(UserModel userModel);

    void deleteUserFromAuthAndUserdataDBs(UserModel userModel);

}
