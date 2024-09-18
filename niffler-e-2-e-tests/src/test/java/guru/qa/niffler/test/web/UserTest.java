package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extantion.UserQueueExtension;
import guru.qa.niffler.jupiter.extantion.UserQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extantion.UserQueueExtension.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class UserTest {

    @ExtendWith(UserQueueExtension.class)
    @Test
    void checkUserEmpty0(@UserType(empty = true) StaticUser user0,
                         @UserType(empty = false) StaticUser user1) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user0);
        System.out.println(user1);
    }

    @ExtendWith(UserQueueExtension.class)
    @Test
    void checkUserEmpty1(@UserType(empty = false) StaticUser user0,
                         @UserType(empty = true) StaticUser user1) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(user1);
        System.out.println(user0);
    }
}