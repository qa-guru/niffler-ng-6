package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.component.SpendingTable;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statBlock = $("#stat");
    private final SearchField searchField = new SearchField($("input[aria-label='search']"));
    private final Header header = new Header();
    private final SpendingTable spendingTable = new SpendingTable();

    public EditSpendingPage editSpending(String spendingDescription) {        ;
        return spendingTable.editSpending(spendingDescription);
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        searchField.search(spendingDescription);
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public ProfilePage openProfilePage() {
        return header.toProfilePage();
    }

    public FriendsPage openFriendsPage() {
        return header.toFriendsPage();
    }

    public AllPeoplePage openAllPeoplePage() {
        return header.toAllPeoplesPage();
    }

    public void checkStatisticBlock() {
        statBlock.should(visible);
    }

}