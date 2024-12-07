package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage<MainPage> {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statBlock = $("#stat");
    private final ElementsCollection deleteSubmitButtons = $$("div[role='dialog'] button");
    private final ElementsCollection categoryContainerComponents = $$("#legend-container li");
    private final SearchField searchField = new SearchField($("input[aria-label='search']"));
    private final SpendingTable spendingTable = new SpendingTable();
    private final StatComponent statComponent = new StatComponent();

    @Step("Открываем страницу для редактирования траты ")
    public EditSpendingPage editSpending(String spendingDescription) {
        return spendingTable.editSpending(spendingDescription);
    }

    @Step("Check status component ")
    public StatComponent getStatComponent() {
        statComponent.getSelf().scrollIntoView(true);
        return statComponent;
    }

    @Step("Check spending table ")
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Открываем страницу для редактирования траты ")
    public MainPage deleteSpending(String spendingDescription) {
        spendingTable.deleteSpending(spendingDescription);
        deleteSubmitButtons.find(text("Delete")).click();
        return this;
    }

    @Step("Проверяем, что в таблице с тратами есть искомая строка по названию траты")
    public void checkThatTableContainsSpending(String spendingDescription) {
        searchField.search(spendingDescription);
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Step("Проверяем в блоке статистки ячейку с категорией и суммой")
    public MainPage checkValueCategory(String  categoryName, String amount) {
        String amountActual =  categoryContainerComponents.findBy(text(categoryName)).getText();
        Assertions.assertTrue(amountActual.contains(amount));
        return this;
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