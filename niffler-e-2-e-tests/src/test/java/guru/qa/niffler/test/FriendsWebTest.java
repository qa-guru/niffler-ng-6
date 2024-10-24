package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension.StaticUser;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static guru.qa.niffler.enums.TypeEnum.EMPTY;
import static guru.qa.niffler.enums.TypeEnum.WITH_FRIEND;
import static guru.qa.niffler.enums.TypeEnum.WITH_INCOME_REQUEST;
import static guru.qa.niffler.enums.TypeEnum.WITH_OUTCOME_REQUEST;

@Feature("UI:Авторизация пользователя в системе")
@WebTest
public class FriendsWebTest extends BaseTest {

  @Test
  @Description("Проверка наличия друзей в таблице дружбы")
  void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user,
                                           @UserType(WITH_INCOME_REQUEST) StaticUser friendUser) {
    logIntoSystem(user.userName(), user.password())
        .goToFriendsTab()
        .checkFriendsIsExist(List.of(friendUser.userName()));
  }

  @Test
  @Description("Проверка отсутствия друзей в таблице дружбы")
  void friendsTableShouldBeEmptyForNewUsers(@UserType(EMPTY) StaticUser user) {
    logIntoSystem(user.userName(), user.password())
        .goToFriendsTab()
        .checkFriendsIsNotExist();
  }

  @Test
  @Description("Проверка наличия входящего запроса дружбы")
  void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user,
                                               @UserType(WITH_OUTCOME_REQUEST) StaticUser incomeFriendUser) {
    logIntoSystem(user.userName(), user.password())
        .goToFriendsTab()
        .checkIncomeFriendsRequest(List.of(incomeFriendUser.userName()));
  }

  @Test
  @Description("Проверка наличия исходящего запроса дружбы")
  void incomeInvitationBePresentInAllPeopleTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user,
                                                 @UserType(WITH_INCOME_REQUEST) StaticUser outcomeFriendUser) {
    logIntoSystem(user.userName(), user.password())
        .goToAllPeopleTab()
        .checkOutcomeFriendsRequest(List.of(outcomeFriendUser.userName()));
  }
}
