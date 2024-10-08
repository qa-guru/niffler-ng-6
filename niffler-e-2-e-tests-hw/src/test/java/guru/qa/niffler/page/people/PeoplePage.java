package guru.qa.niffler.page.people;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.AppHeader;
import guru.qa.niffler.page.BasePage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
public abstract class PeoplePage<T> extends BasePage<T> {

    public PeoplePage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    protected final SelenideElement friendsTab = $x("//a[./h2[text()='Friends']]").as("Friends tab"),
            allPeopleTab = $x("//a[./h2[text()='All people']]").as("All people tab"),
            searchInput = $("input[placeholder='Search']").as("Search input"),
            clearSearchQueryButton = $("#input-clear").as("Clear search query button"),
            nextPageButton = $("#page-next").as("'Next' button"),
            previousPageButton = $("#page-previous").as("'Previous' button"),
            allPeopleTableContainer = $(byText("#friends")).as("Friends list title");

    protected final By usernameSelector = byXpath(".//p[1]"),
            waitingButtonSelector = byCssSelector("span[class*='label']"),
            addFriendButtonSelector = byCssSelector("button[class*='label']");

    protected final ElementsCollection allPeopleList = allPeopleTableContainer.$$("tr").as("'All people' list");

    public AppHeader getHeader() {
        return new AppHeader();
    }

    @SuppressWarnings("unchecked")
    public T filterByQuery(String query) {
        log.info("Filter users by query = [{}]", query);
        searchInput.click();
        searchInput.setValue(query).pressEnter();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T previousPage() {
        log.info("Show previous page");
        previousPageButton.shouldHave(cssClass("colorPrimary")).click();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T nextPage() {
        log.info("Show next page");
        nextPageButton.shouldHave(cssClass("colorPrimary")).click();
        return (T) this;
    }


}
