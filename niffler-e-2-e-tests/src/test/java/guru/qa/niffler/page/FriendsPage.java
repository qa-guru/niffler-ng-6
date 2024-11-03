package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.component.FriendsRequestTable;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage extends BasePage<FriendsPage> {
    private final SelenideElement buttonToOpenAllPeoplePage = $("a[href='/people/all']");
    private final ElementsCollection listFriends = $$("#friends tr");
    private final SearchField searchField = new SearchField($("input[aria-label='search']"));
    private final FriendsRequestTable friendsRequestTable = new FriendsRequestTable();
    private final SelenideElement declineFromDialogButton = $("div[role='dialog'] button span svg");

    @Step("Открываем страницу со списком друзей")
    public AllPeoplePage openAllPeoplePage() {
        buttonToOpenAllPeoplePage.click();
        return new AllPeoplePage();
    }

    @Step("Проверяем, что в таблице с друзьями есть запись ")
    public void checkHaveFriend() {
        listFriends.find(text("Unfriend")).should(visible);
    }

    @Step("Ищем пользователя в таблице друзей")
    public FriendsPage findFriend(String searchString) {
        searchField.search(searchString);
       return this;
    }

    @Step("Проверяем, что есть входящее предложение дружбы")
    public void checkIncomeInvitationFriend() {
        friendsRequestTable.checkIncomeInvitationFriend();
    }

    @Step("Проверяем, что у пользователя нет друзей ")
    public void checkNotHaveFriend() {
        listFriends.shouldHave(size(0));
    }

    @Step("Принимаем предложение дружбы")
    public FriendsPage acceptFriendship(String username) {
        friendsRequestTable.acceptFriendRequest(username);
        return this;
    }

    @Step("Отклоняем предложение дружбы")
    public FriendsPage declineFriendship(String username) {
        friendsRequestTable.declineFriendRequest(username);
        declineFromDialogButton.click();
        return this;
    }

}