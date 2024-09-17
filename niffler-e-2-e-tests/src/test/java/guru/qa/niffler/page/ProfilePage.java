package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    private final SelenideElement archiveBtn = $("[aria-label='Archive category']").as("кнопка архивировать");
    private final SelenideElement archiveSubmitBtn = $$("[role='dialog'] button").get(1)
            .as("кнопка 'Archive' в форме подтверждения архивации");
    private final SelenideElement successArchiveTooltip = $x("//div[@class='MuiAlert-message css-1xsto0d']")
            .as("тултип с информацией об успешной архивации");
    private final SelenideElement showArchivedSwitcher = $x("//span[text()='Show archived']")
            .as("свитчер включающий показ заархивированных категорий");
    private final ElementsCollection categoryItem = $$("[class*='MuiChip-filled']").as("строка с категорий");
    private final SelenideElement unArchiveSubmitBtn = $("[aria-label='Unarchive category']").as("кнопка разархивировать");

    @Step("Кликнуть по кнопке архивации категории")
    public ProfilePage clickArchiveButton() {
        archiveBtn.click();
        return this;
    }

    @Step("Кликнуть по кнопке разархивации категории")
    public ProfilePage clickUnArchiveButton() {
        unArchiveSubmitBtn.click();
        return this;
    }

    @Step("Кликнуть по кнопке подтверждения архивации")
    public ProfilePage confirmArchiving() {
        archiveSubmitBtn.click();
        return this;
    }

    @Step("Появилась плашка с информацией об успешной архивации категории")
    public ProfilePage successArchiveTooltipCheck(String message) {
        successArchiveTooltip.shouldHave(text(message));
        return this;
    }

    @Step("Включить показ заархивированных категорий")
    public ProfilePage clickArchiveSwitcher() {
        showArchivedSwitcher.click();
        return this;
    }

    @Step("Проверка того, что категория отображается в списке")
    public ProfilePage categoryInListCheck(String categoryName) {
        categoryItem.findBy(text(categoryName)).should(exist);
        return this;
    }

    @Step("Проверка того, что категория НЕ отображается в списке")
    public ProfilePage categoryNotInListCheck(String categoryName) {
        categoryItem.findBy(text(categoryName)).shouldNotHave(exist);
        return this;
    }
}
