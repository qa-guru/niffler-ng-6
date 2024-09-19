package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement archiveButton = $$(".MuiIconButton-sizeMedium").get(1);
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement activeCategoryList = $(".MuiChip-filled");
    private final SelenideElement successMessage = $(".MuiTypography-body1");
    private final SelenideElement showArchivedButton = $x("//span[text()='Show archived']");


    public ProfilePage shouldForCategoryName(String categoryName) {
        categoryList.filterBy(text(categoryName))
                .first()
                .shouldHave(text(categoryName));
        return this;
    }


    public ProfilePage shouldActiveCategoryList(String value) {
        activeCategoryList.shouldHave(text(value));
        return this;
    }


    public ProfilePage clickArchiveButton() {
        archiveButton.click();
        return this;
    }

    public ProfilePage clickArchiveButtonSubmit() {
        archiveButtonSubmit.click();
        return this;
    }

    public ProfilePage shouldBeVisibleSuccessMessage() {
        successMessage.shouldBe(visible);
        return this;
    }

    public ProfilePage clickShowArchivedButton() {
        showArchivedButton.click();
        return this;
    }



}
