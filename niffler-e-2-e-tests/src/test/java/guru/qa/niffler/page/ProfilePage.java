package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement archiveButton = $$(".MuiIconButton-sizeMedium").get(1);
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement unArchiveButtonSubmit = $x("//button[text()='Unarchive']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement successArchiveMessage = $x("//div[@class='MuiAlert-message css-1xsto0d']");
    private final SelenideElement successUnArchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement showArchiveCategoryButton = $x("//input[@type='checkbox']");
    private final SelenideElement activeCategoryList = $(".MuiChip-filled");
    private final SelenideElement archiveCategoryList = $(".MuiFormControlLabel-label css-8va9ha");
    private final SelenideElement archiveList = $(".css-14vsv3w");
    private final SelenideElement successMessage = $(".MuiTypography-body1");
    private final SelenideElement showArchivedButton = $x("//span[text()='Show archived']");


    public ProfilePage shouldForCategoryName(String categoryName) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).text().equals(categoryName)) {
                categoryList.get(i).shouldHave(text(categoryName));
                break;
            }
        }
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
