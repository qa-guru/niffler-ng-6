package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    private final SelenideElement archiveButton = $$(".MuiIconButton-sizeMedium").get(1);
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement activeCategoryList = $(".MuiChip-filled");
    private final SelenideElement archiveCategoryList = $(".MuiFormControlLabel-label css-8va9ha");
    private final SelenideElement successMessage = $(".MuiTypography-body1");

    public ProfilePage shouldActiveCategoryList(String value) {
        activeCategoryList.shouldHave(text(value));
        return this;
    }

    public ProfilePage clickArchiveCategoryList() {
        archiveCategoryList.click();
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


}
