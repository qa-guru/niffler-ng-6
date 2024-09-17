package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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
    private final SelenideElement avatar = $("#image__input").parent().$("img");
    private final SelenideElement userName = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement photoInput = $("input[type='file']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

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

    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage uploadPhotoFromClasspath(String path) {
        photoInput.uploadFromClasspath(path);
        return this;
    }

    public ProfilePage addCategory(String category) {
        categoryInput.setValue(category).pressEnter();
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        archivedSwitcher.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkUsername(String username) {
        this.userName.should(value(username));
        return this;
    }

    public ProfilePage checkName(String name) {
        nameInput.shouldHave(value(name));
        return this;
    }

    public ProfilePage checkPhotoExist() {
        avatar.should(attributeMatching("src", "data:image.*"));
        return this;
    }

    public ProfilePage checkThatCategoryInputDisabled() {
        categoryInput.should(disabled);
        return this;
    }

    public ProfilePage submitProfile() {
        submitButton.click();
        return this;
    }
}
