package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.GatewayApiClient;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.ThreadSafeCookieStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.lang.annotation.Target;
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
                u -> u.friendState() == FriendState.FRIEND
        ).toList();

        final List<UserJson> invitationsFromResponse = result.stream().filter(
                u -> u.friendState() == FriendState.INVITE_RECEIVED
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

    @User(friends = 3, incomeInvitations = 2)
    @ApiLogin
    @Test
    void allFriendsAndIncomeInvitationsShouldBeReturnedFroUserFromFilter(@Token String token, UserJson user) {
        List<UserJson> allFriends = gatewayApiClient.allFriends(token, null);
        long countFriends = allFriends.stream()
                .filter(u -> u.friendState() == FriendState.FRIEND)
                .count();

        long countIncomeInvitation = allFriends.stream()
                .filter(u -> u.friendState() == FriendState.INVITE_RECEIVED)
                .count();
        Assertions.assertTrue(countFriends == 3, "Count expected friends  match actual count friends");
        Assertions.assertTrue(countIncomeInvitation == 2, "Count expected income invitation  match actual count income invitation");
    }

    @User(friends = 1)
    @ApiLogin
    @Test
    void deleteFriendship(@Token String token, UserJson user) {
        UserJson friends = user.testData().friends().getFirst();
        gatewayApiClient.removeFriends(token, friends.username());
        List<UserJson> listFriends = gatewayApiClient.allFriends(token, friends.username());
        Assertions.assertTrue(listFriends.stream().filter(u -> u.friendState() == FriendState.FRIEND).toList().size() == 0,
                "List friends not contain expected username");
    }


    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void acceptIncomeInvitation(@Token String token, UserJson user) {
        UserJson friendJson = user.testData().incomeInvitations().getFirst();
        FriendJson friend = new FriendJson(friendJson.username());
        gatewayApiClient.acceptFriends(token, friend);
        List<UserJson> listFriends = gatewayApiClient.allFriends(token, friend.username());
        Assertions.assertTrue(listFriends.stream().filter(u -> u.friendState() == FriendState.FRIEND).toList().size() == 1,
                "List friends contain expected username");
    }


    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void declineIncomeInvitation(@Token String token, UserJson user) {
        UserJson friendJason = user.testData().incomeInvitations().getFirst();
        FriendJson friend = new FriendJson(friendJason.username());
        gatewayApiClient.declineInvitation(token, friend);
        List<UserJson> listFriends = gatewayApiClient.allFriends(token, friend.username());
        Assertions.assertTrue(listFriends.stream().filter(u -> u.friendState() == FriendState.FRIEND).toList().size() == 0,
                "List friends not contain expected username");
        Assertions.assertTrue(listFriends.stream().filter(u -> u.friendState() == FriendState.INVITE_RECEIVED).toList().size() == 0,
                "List income invitation not contain expected username");
    }

    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void checkIncomeAndOutcomeInvitation(@Token String token, UserJson user) {
        String friendName = user.testData().outcomeInvitations().getFirst().username();
        UserJson friendJson = gatewayApiClient.allUsers(token, friendName).getFirst();
        Assertions.assertTrue(friendJson.friendState() == FriendState.INVITE_SENT,
                "Target user have income invitation");
        ThreadSafeCookieStore.INSTANCE.removeAll();
        String tokenFriend = "Bearer " + new AuthApiClient().getToken(friendName, "12345");
        UserJson userState = gatewayApiClient.allFriends(tokenFriend, user.username()).getFirst();
        Assertions.assertTrue(userState.friendState() == FriendState.INVITE_RECEIVED,
                "Target user have income invitation");
    }

}
