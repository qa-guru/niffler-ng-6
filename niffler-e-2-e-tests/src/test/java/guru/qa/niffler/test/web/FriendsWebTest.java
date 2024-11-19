package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtensions.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtensions.UserType.Type.*;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIENDS) StaticUser user) {

    }

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    void friendTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {

    }

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    void incomeInvatationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {

    }

    @Test
    @ExtendWith(UsersQueueExtensions.class)
    void outcomeInvatationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {

    }
}
