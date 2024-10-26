package guru.qa.niffler.page.component;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header<T extends BasePage<?>> extends BaseComponent<T> {
    private final SelenideElement menu = $("ul[role='menu']");

    public Header(SelenideElement header, T page) {
        super(header, page);
    }

    @Nonnull
    @Step("Перейти на \"Friends\" страницу")
    public FriendsPage toFriendsPage() {
        self.$("button").click();
        menu.$$("li").find(text("Friends")).click();
        return new FriendsPage();
    }

    @Nonnull
    @Step("Перейти на \"All People\" страницу")
    public FriendsPage toAllPeoplesPage() {
        self.$("button").click();
        menu.$$("li").find(text("All People")).click();
        return new FriendsPage();
    }

    @Nonnull
    @Step("Перейти на страницу профиля")
    public ProfilePage toProfilePage() {
        self.$("[aria-label='Menu']").click();
        menu.$(byText("Profile")).click();
        return new ProfilePage();
    }

    @Nonnull
    @Step("Перейти на главную страницу")
    public MainPage toMainPage() {
        self.$(".MuiToolbar-gutters").click();
        return new MainPage();
    }

    @Nonnull
    @Step("Разлогинить пользователя")
    public LoginPage signOut() {
        self.$("[aria-label='Menu']").click();
        menu.$(byText("Sign out")).click();
        return new LoginPage();
    }

    @Nonnull
    @Step("Добавить новую трату")
    public EditSpendingPage addSpendingPage() {
        self.$(byText("New spending")).click();
        return new EditSpendingPage();
    }

    @Step("Проверить заголовок в хедере")
    public void checkHeaderText() {
        self.$("h1").shouldHave(text("Niffler"));
    }
}