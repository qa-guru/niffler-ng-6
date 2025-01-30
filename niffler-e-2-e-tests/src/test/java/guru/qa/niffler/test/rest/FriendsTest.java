package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

@RestTest
public class FriendsTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

  @User(friends = 2, incomeInvitations = 1)
  @ApiLogin
  @Test
  void allFriendsAndIncomeInvitationsShouldBeReturnedFroUser(UserJson user, @Token String token) {
    final List<UserJson> expectedFriends = user.testData().friends();
    final List<UserJson> expectedInvitations = user.testData().incomeInvitations();

    final List<UserJson> result = gatewayApiClient.allFriends(
        token,
        null
    );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(3, result.size());

    final List<UserJson> friendsFromResponse = result.stream().filter(
        u -> u.friendshipStatus() == FriendshipStatus.FRIEND
    ).toList();

    final List<UserJson> invitationsFromResponse = result.stream().filter(
        u -> u.friendshipStatus() == FriendshipStatus.INVITE_RECEIVED
    ).toList();

    Assertions.assertEquals(2, friendsFromResponse.size());
    Assertions.assertEquals(1, invitationsFromResponse.size());

    Assertions.assertEquals(
        expectedInvitations.getFirst().username(),
        invitationsFromResponse.getFirst().username()
    );

    final UserJson firstUserFromRequest = friendsFromResponse.getFirst();
    final UserJson secondUserFromRequest = friendsFromResponse.getLast();

    Assertions.assertEquals(
        expectedFriends.getFirst().username(),
        firstUserFromRequest.username()
    );

    Assertions.assertEquals(
        expectedFriends.getLast().username(),
        secondUserFromRequest.username()
    );
  }

}
