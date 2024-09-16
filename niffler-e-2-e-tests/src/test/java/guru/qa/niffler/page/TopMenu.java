package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TopMenu {
    private final SelenideElement personIconButton = $("button[aria-label='Menu']");
    private final SelenideElement goToProfileButton = $("[href='/profile']");
    private final SelenideElement goToFriendsButton = $("[href='/people/friends']"); //
    private final SelenideElement goToAllPeopleButton = $("[href='/people/all']");

    public ProfilePage goToProfilePage() {
        personIconButton.click();
        goToProfileButton.click();
        return new ProfilePage();
    }

    public FriendsPage goToFriendsPage() {
        personIconButton.click();
        goToFriendsButton.click();
        return new FriendsPage();
    }

    public FriendsPage goToAllPeoplePage() {
        personIconButton.click();
        goToAllPeopleButton.click();
        return new FriendsPage();
    }
}
