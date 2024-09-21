package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final ElementsCollection friendsList = $$("#friends tr").as("список друзей");
    private final ElementsCollection requestsList = $$("#requests tr").as("список заявок в друзья");
    private final SelenideElement allPeopleBtn = $(byText("All people")).as("кнопка 'All people'");

    @Step("Проверка того, что у пользователя есть добавленные друзья")
    public FriendsPage existingFriendsCheck(String... expectedUsernames) {
        friendsList.shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Проверка того, что у пользователя нет друзей")
    public FriendsPage noExistingFriendsCheck() {
        friendsList.shouldHave(size(0));
        return this;
    }

    @Step("Проверка того, что у пользователя есть заявка в друзья")
    public FriendsPage invitationsToFriendsCheck(String... expectedUsernames) {
        requestsList.shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Кликнуть по кнопке 'AllPeople'")
    public PeoplePage clickAllPeopleBtn() {
        allPeopleBtn.click();
        return new PeoplePage();
    }
}
