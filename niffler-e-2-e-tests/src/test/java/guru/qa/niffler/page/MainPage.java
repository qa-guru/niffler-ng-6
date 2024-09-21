package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsHeader = $("[id='stat'] h2").as("хедер блока 'Statistics'");
    private final SelenideElement statisticsImg = $("[id='stat'] [role='img']").as("диаграмма блока 'Statistics'");
    private final SelenideElement spendingsHeader = $("[id='spendings'] h2").as("хедер блока 'History of Spendings'");
    private final SelenideElement userMenuBtn = $("[aria-label='Menu']").as("кнопка открытия меню");
    private final SelenideElement profileBtn = $(byText("Profile")).as("кнопка открытия профиля");
    private final SelenideElement friendsBtn = $(byText("Friends")).as("кнопка открытия профиля");

    @Step("Нажать на кнопку редактирования траты")
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Step("Проверка того, что в списке есть ожидаемая трата")
    public MainPage checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
        return this;
    }

    @Step("Отображаются основные элементы главной страницы")
    public MainPage mainPageAfterLoginCheck() {
        statisticsHeader.shouldBe(visible).shouldHave(text("Statistics"));
        statisticsImg.shouldBe(visible);
        spendingsHeader.shouldBe(visible).shouldHave(text("History of Spendings"));
        return this;
    }

    @Step("Открыть профиль")
    public ProfilePage openProfile() {
        userMenuBtn.click();
        profileBtn.click();
        return new ProfilePage();
    }

    @Step("Открыть вкладку 'Друзья'")
    public FriendsPage openFriendsPage() {
        userMenuBtn.click();
        friendsBtn.click();
        return new FriendsPage();
    }
}

