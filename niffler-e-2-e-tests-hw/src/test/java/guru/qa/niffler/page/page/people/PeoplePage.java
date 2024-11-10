package guru.qa.niffler.page.page.people;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.page.page.BasePage;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public abstract class PeoplePage<T> extends BasePage<T> {

    public PeoplePage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    protected final SelenideElement friendsTab = $x("//a[./h2[text()='Friends']]").as("Friends tab"),
            allPeopleTab = $x("//a[./h2[text()='All people']]").as("All people tab"),
            nextPageButton = $("#page-next").as("'Next' button"),
            previousPageButton = $("#page-previous").as("'Previous' button");

    protected final By usernameSelector = byXpath(".//p[1]"),
            waitingButtonSelector = byCssSelector("span[class*='label']"),
            addFriendButtonSelector = byCssSelector("button");

    private final SearchField search = new SearchField($("form[class^=MuiBox-root]"));

    public Header getHeader() {
        return new Header();
    }

    @SuppressWarnings("unchecked")
    @Step("Filter users by query = [{}]")
    public T filterByQuery(String query) {
        log.info("Filter users by query = [{}]", query);
        search.clearByButton();
        search.setValue(query);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Go to previous page")
    public T previousPage() {
        log.info("Show previous page");
        previousPageButton.shouldHave(cssClass("colorPrimary")).click();
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Go to next page")
    public T nextPage() {
        log.info("Show next page");
        nextPageButton.shouldHave(cssClass("colorPrimary")).click();
        return (T) this;
    }


}
