package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statBlock = $("#stat");
    private final SearchField searchField = new SearchField($("input[aria-label='search']"));
    private final Header header = new Header();
    private final SpendingTable spendingTable = new SpendingTable();

    @Step("Открываем страницу для редактирования траты ")
    public EditSpendingPage editSpending(String spendingDescription) {
        return spendingTable.editSpending(spendingDescription);
    }

    @Step("Проверяем, что в таблице с тратами есть искомая строка по названию траты")
    public void checkThatTableContainsSpending(String spendingDescription) {
        searchField.search(spendingDescription);
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Step("Открываем страницу профиля")
    public ProfilePage openProfilePage() {
        return header.toProfilePage();
    }

    @Step("Открываем станицу со списком друзей")
    public FriendsPage openFriendsPage() {
        return header.toFriendsPage();
    }

    @Step("Открываем станицу со списком всех людей")
    public AllPeoplePage openAllPeoplePage() {
        return header.toAllPeoplesPage();
    }
    @Step("Проверяем, что блок статистики отображается")
    public void checkStatisticBlock() {
        statBlock.should(visible);
    }

    @Step("Открываем страницу для создания новой траты")
    public EditSpendingPage openNewSpending() {
        return header.addSpendingPage();
    }

}