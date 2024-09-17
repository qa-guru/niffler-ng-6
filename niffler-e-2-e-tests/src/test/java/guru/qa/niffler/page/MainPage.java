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
    private final SelenideElement spendingsTable = $("[id='spendings'] [aria-labelledby='tableTitle']").as("таблица с тратами");
    private final SelenideElement userMenuBtn = $("[aria-label='Menu']").as("кнопка открытия меню");
    private final SelenideElement profileBtn = $(byText("Profile")).as("кнопка открытия профиля");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Step("Отображаются основные элементы главной страницы")
    public void mainPageAfterLoginCheck() {
        statisticsHeader.shouldBe(visible).shouldHave(text("Statistics"));
        statisticsImg.shouldBe(visible);
        spendingsHeader.shouldBe(visible).shouldHave(text("History of Spendings"));
        spendingsTable.shouldBe(visible);
    }

    @Step("Кликнуть по кнопке открытия меню")
    public void clickUserMenuBtn() {
        userMenuBtn.click();
    }

    @Step("Открыть профиль")
    public void openProfile() {
        userMenuBtn.click();
        profileBtn.click();
    }
}

