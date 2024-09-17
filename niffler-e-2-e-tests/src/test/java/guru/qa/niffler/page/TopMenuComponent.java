package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

class TopMenuComponent {
    private final SelenideElement personIconButton = $("button[aria-label='Menu']");
    private final SelenideElement goToProfileButton = $("[href='/profile']");

    public ProfilePage goToProfile() {
        personIconButton.click();
        goToProfileButton.click();
        return new ProfilePage();
    }
}
