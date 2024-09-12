package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement unarchiveButtonSubmit = $x("//button[text()='Unarchive']");
    private final SelenideElement successArchiveMessage = $(".MuiAlert-message .MuiTypography-body1");
    private final SelenideElement showArchiveSwitcher = $(".MuiFormControlLabel-root");

    public ProfilePage clickArchiveButtonForCategory(String categoryName) {
        categoryList
                .findBy(text(categoryName))
                .parent()
                .$(".MuiIconButton-sizeMedium[aria-label='Archive category']")
                .click();
        return this;
    }

    public ProfilePage clickUnarchiveButtonForCategory(String categoryName) {
        categoryList
                .findBy(text(categoryName))
                .parent()
                .$("[data-testid='UnarchiveOutlinedIcon']")
                .click();
        return this;
    }

    public ProfilePage clickShowArchiveCategoryButton() {
        showArchiveSwitcher.click();
        return this;
    }

    public ProfilePage clickArchiveButtonSubmit() {
        archiveButtonSubmit.click();
        return this;
    }

    public ProfilePage clickUnarchiveButtonSubmit() {
        unarchiveButtonSubmit.click();
        return this;
    }

    public ProfilePage shouldBeVisibleArchiveSuccessMessage(String value) {
        successArchiveMessage.shouldHave(text("Category " + value + " is archived")).shouldBe(visible);
        return this;
    }

    public ProfilePage shouldBeVisibleUnarchiveSuccessMessage(String value) {
        successArchiveMessage.shouldHave(text("Category " + value + " is unarchived")).shouldBe(visible);
        return this;
    }

    public ProfilePage shouldVisibleActiveCategory(String value) {
        categoryList.findBy(text(value)).shouldBe(visible);
        return this;
    }

    public ProfilePage shouldNotVisibleArchiveCategory(String value) {
        categoryList.findBy(text(value)).shouldNotBe(visible);
        return this;
    }
}
