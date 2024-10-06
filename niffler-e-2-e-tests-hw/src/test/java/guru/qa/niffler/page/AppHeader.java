package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.spending.AddNewSpendingPage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@NoArgsConstructor
public class AppHeader extends BasePage<AppHeader> {

    public AppHeader(boolean checkPageElementVisible){
        super(checkPageElementVisible);
    }

    private final SelenideElement header = $("header").as("[Header]");
    private final SelenideElement logoImg = header.$("a[href='main'] img").as("['Logo' image]");
    private final SelenideElement logoTitle = header.$("a[href='main'] h1").as("['Logo' title]");
    private final SelenideElement newSpendingButton = header.$("a[href='/spending']").as("['New spending' button]");
    private final SelenideElement avatarButton = header.$("button[aria-label='Menu']").as("['Menu' button]");

    public MainPage mainPage() {
        log.info("Go to main page");
        logoTitle.click();
        return new MainPage();
    }

    public AddNewSpendingPage goToCreateSpendingPage() {
        log.info("Open 'Create new spending' page");
        newSpendingButton.click();
        return new AddNewSpendingPage(true);
    }

    public AccountMenuPage openUserMenu() {
        log.info("Open user menu");
        avatarButton.click();
        return new AccountMenuPage();
    }

    @Override
    public AppHeader shouldVisiblePageElement() {
        log.info("Assert 'App Header' page element visible on start up");
        header.shouldBe(visible);
        return this;
    }

    @Override
    public AppHeader shouldVisiblePageElements() {

        log.info("Assert app header elements are visible");

        logoImg.shouldBe(visible);
        logoTitle.shouldBe(visible);
        newSpendingButton.shouldBe(visible);
        avatarButton.shouldBe(visible);
        logoImg.shouldBe(visible);

        return this;

    }

}