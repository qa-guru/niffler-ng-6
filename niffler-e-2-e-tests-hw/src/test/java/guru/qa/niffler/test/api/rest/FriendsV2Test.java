package guru.qa.niffler.test.api.rest;

import guru.qa.niffler.api.GatewayApiV2Client;
import guru.qa.niffler.enums.FriendState;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.model.rest.pageable.RestResponsePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@RestTest
class FriendsV2Test {

    private final GatewayApiV2Client gatewayApiV2Client = new GatewayApiV2Client();

    @Test
    void allFriendsAndIncomeInvitationsShouldBeReturnedFroUser(
            @ApiLogin
            @CreateNewUser(
                    friends = 2,
                    incomeInvitations = 1
            )
            UserJson user
    ) {
        final List<UserJson> expectedFriends = user.getTestData().getFriends();
        final List<UserJson> expectedInvitations = user.getTestData().getIncomeInvitations();

        final RestResponsePage<UserJson> result = gatewayApiV2Client.allFriends(
                user.getTestData().getToken(),
                null,
                0,
                "username,ASC"
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getContent().size());

        final List<UserJson> friendsFromResponse = result.stream().filter(
                u -> u.getFriendState() == FriendState.FRIEND
        ).toList();

        final List<UserJson> invitationsFromResponse = result.stream().filter(
                u -> u.getFriendState() == FriendState.INVITE_RECEIVED
        ).toList();

        Assertions.assertEquals(2, friendsFromResponse.size());
        Assertions.assertEquals(1, invitationsFromResponse.size());

        Assertions.assertEquals(
                expectedInvitations.getFirst().getUsername(),
                invitationsFromResponse.getFirst().getUsername()
        );

        final UserJson firstUserFromRequest = friendsFromResponse.getFirst();
        final UserJson secondUserFromRequest = friendsFromResponse.getLast();

        Assertions.assertEquals(
                expectedFriends.getFirst().getUsername(),
                firstUserFromRequest.getUsername()
        );

        Assertions.assertEquals(
                expectedFriends.getLast().getUsername(),
                secondUserFromRequest.getUsername()
        );
    }

}
