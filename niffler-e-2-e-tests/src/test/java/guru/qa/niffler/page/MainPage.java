package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl() + "main";

    protected final Header header = new Header();
    protected final SpendingTable spendingTable = new SpendingTable();
    protected final StatComponent statComponent = new StatComponent();
    // Коллекция всех ячеек статистики
    private final ElementsCollection statisticCells = $$("#legend-container li");

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Check that page is loaded")
    @Override
    @Nonnull
    public MainPage checkThatPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler"));
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
        return this;
    }

    @Step("Check that statistic cells contain texts {texts}")
    @Nonnull
    public MainPage checkStatisticCells(List<String> texts) {
        for (String text : texts) {
            statisticCells.findBy(text(text)).shouldBe(visible);
        }
        return this;
    }
}