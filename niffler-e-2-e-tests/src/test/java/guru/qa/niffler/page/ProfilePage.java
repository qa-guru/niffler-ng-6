package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement saveChangesButton = $("button[type='submit']");

    public ProfilePage setUsername(String username) {
        usernameInput.setValue(username);
        return new ProfilePage();
    }

    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return new ProfilePage();
    }

    public ProfilePage submitSaveChangeButton() {
        saveChangesButton.click();
        return new ProfilePage();
    }
}