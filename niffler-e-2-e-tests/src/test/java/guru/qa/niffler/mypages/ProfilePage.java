package guru.qa.niffler.mypages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement profileHeader = $x("//*[text()='Profile']");

    private final ElementsCollection categories = $$("div[class*='MuiGrid-spacing-xs-2'] > div");

    private final SelenideElement showArchivedBtn = $x("//span[text()='Show archived']");

    public ProfilePage checkProfilePage() {
        profileHeader.shouldBe(visible);
        return this;
    }

    public void checkCategory(String categoryName) {
        categories.find(text(categoryName)).shouldBe(visible);
    }

    public ProfilePage showArchived() {
        showArchivedBtn.shouldBe(visible)
                .click();
        return this;
    }
}
