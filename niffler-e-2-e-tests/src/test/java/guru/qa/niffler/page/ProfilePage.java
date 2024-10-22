package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

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

    @Step("Проверить название категории {categoryName}")
    public ProfilePage shouldForCategoryName(String categoryName) {
        categoryList.filterBy(text(categoryName))
                .first()
                .shouldHave(text(categoryName));
        return this;
    }

    @Step("Проверить название категории {value}")
    public ProfilePage shouldActiveCategoryList(String value) {
        activeCategoryList.shouldHave(text(value));
        return this;
    }

    @Step("Нажать на кнопку Архивные")
    public ProfilePage clickArchiveButton() {
        archiveButton.click();
        return this;
    }

    @Step("Нажать на кнопку подтверждения")
    public ProfilePage clickArchiveButtonSubmit() {
        archiveButtonSubmit.click();
        return this;
    }

    @Step("Проверить видимость успешного сообщения")
    public ProfilePage shouldBeVisibleSuccessMessage() {
        successMessage.shouldBe(visible);
        return this;
    }

    @Step("Нажать на кнопку Показать архивные")
    public ProfilePage clickShowArchivedButton() {
        showArchivedButton.click();
        return this;
    }


}
