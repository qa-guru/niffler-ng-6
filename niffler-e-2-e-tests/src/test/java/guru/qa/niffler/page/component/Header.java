package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class Header {
    private final SelenideElement menu = $("ul[role='menu']");
    private final SelenideElement header = $("#root header");

    @Step("Перейти на страницу Friends")
    public FriendsPage toFriendsPage() {
        header.$("button").click();
        menu.$$("li").find(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Перейти на страницу Profile")
    public ProfilePage toProfilePage() {
        header.$("[aria-label='Menu']").click();
        menu.$(byText("Profile")).click();
        return new ProfilePage();
    }

    @Step("Перейти на страницу All People")
    public PeoplePage toAllPeoplesPage() {
        header.$("button").click();
        menu.$$("li").find(text("All People")).click();
        return new PeoplePage();
    }

    @Step("Перейти на главную страницу")
    public MainPage toMainPage() {
        header.$(".MuiToolbar-gutters").click();
        return new MainPage();
    }

    @Step("Выход")
    public LoginPage signOut() {
        header.$("[aria-label='Menu']").click();
        menu.$(byText("Sign out")).click();
        return new LoginPage();
    }

    @Step("Добавить Spending")
    public EditSpendingPage addSpendingPage() {
        header.$(byText("New spending")).click();
        return new EditSpendingPage();
    }
}
