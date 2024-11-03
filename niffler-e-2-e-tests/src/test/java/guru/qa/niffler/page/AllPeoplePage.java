package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage extends BasePage<AllPeoplePage> {
    private final SelenideElement bottonToOpenFriendsPage = $("a[href='/people/friends']");
    private final ElementsCollection listAllPeople = $$("#all tr");
    private final SearchField searchField = new SearchField($("input[aria-label='search']"));

    @Step("Открываем страницу со списком друзей")
    public FriendsPage openFriendsPeoplePage() {
        bottonToOpenFriendsPage.click();
        return new FriendsPage();
    }

    @Step("Ищем в таблице со списком людей записи по заданной строке")
    public AllPeoplePage toSearch(String searchString) {
        searchField.search(searchString);
        return this;
    }

    @Step("Проверяем, что есть исходящее предложение дружбы")
    public void checkHaveOutcomeInvitation() {
        listAllPeople.find(text("Waiting...")).should(visible);
    }

    @Step("Проверяем что в списке нет исходящих предложений дружбы")
    public void checkNotHaveOutcomeInvitation() {
        listAllPeople.find(text("Waiting...")).should(disappear);
    }
}