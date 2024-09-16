package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement archiveSubmitButton = $x("//button[text()='Archive']");
    private final SelenideElement unarchiveSubmitButton = $x("//button[text()='Unarchive']");
    private final SelenideElement archiveSwitcher = $x("//input[@type='checkbox']");
    private final SelenideElement successMessage = $x("//div[@role='alert']");

    public ProfilePage clickShowArchiveCategorySwitcher() {
        archiveSwitcher.click();
        return this;
    }

    public ProfilePage clickArchiveCategoryByName(String categoryName) {
        categoryList.findBy(text(categoryName))
                .parent()
                .$("button[aria-label='Archive category']")
                .click();

        return this;
    }

    public ProfilePage clickArchiveSubmitButton() {
        archiveSubmitButton.click();
        return this;
    }

    public ProfilePage clickUnArchiveSubmitButton() {
        unarchiveSubmitButton.click();
        return this;
    }

    public ProfilePage successMessageShouldBeVisible(String message) {
        successMessage.shouldHave(text(message));
        return this;
    }

    public ProfilePage checkCategoryNotVisible(String categoryName) {
        categoryList.findBy(text(categoryName)).shouldNotBe(visible);
        return this;
    }

    public ProfilePage checkCategoryVisible(String categoryName) {
        categoryList.findBy(text(categoryName)).shouldBe(visible);
        return this;
    }

    public ProfilePage clickUnarchiveCategory(String categoryName) {
        categoryList.findBy(text(categoryName))
                .parent()
                .$("[data-testid='UnarchiveOutlinedIcon']")
                .click();

        return this;
    }
}
