package guru.qa.niffler.test;

import guru.qa.niffler.enums.TypeEnum;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.UserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserQueueExtension.class)
public class ProfileTest {

  @Test
  void testWithEmptyUser(@UserType(TypeEnum.EMPTY) StaticUser user,
                         @UserType(TypeEnum.WITH_INCOME_REQUEST) StaticUser user1
                         ) throws InterruptedException {
    Thread.sleep(1000);
    System.out.println(user);
    System.out.println(user1);
  }

//  @Test
//  void testWithEmptyUser1(@UserType(empty = false) StaticUser user) throws InterruptedException {
//    Thread.sleep(1000);
//    System.out.println(user);
//  }
}
