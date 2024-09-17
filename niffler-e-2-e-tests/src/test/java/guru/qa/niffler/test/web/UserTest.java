package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.extantion.UsersQueueExtension;
import guru.qa.niffler.jupiter.extantion.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extantion.UsersQueueExtension.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class UserTest {

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserEmpty0(@UserType(empty = true) StaticUser user0,
                         @UserType(empty = false) StaticUser user1 ) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("test "+user0);
        System.out.println("test "+user1);
    }

    @ExtendWith(UsersQueueExtension.class)
    @Test
    void checkUserEmpty1(@UserType(empty = false) StaticUser user) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("test "+user);
    }
}
