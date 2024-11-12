package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {
  @Nonnull
  UserJson createUser(String username, String password);

  void addIncomeInvitation(UserJson targetUser, int count);

  void addOutcomeInvitation(UserJson targetUser, int count);

  void addFriend(UserJson targetUser, int count);
}
