package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.io.File;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement usernameInput = $("#username").as("['Username' input]"),
            nameInput = $("#name").as("['Name' input]"),
            showArchivedButton = $x("//span[text()='Show archived']").as("['Show archived' checkbox]"),
            showArchivedStatusButton = $("input[type='checkbox']").parent().as("['Show archived' checkbox]"),
            categoryInput = $("[placeholder='Add new category']").as("['Categories search' input]"),
            saveChangesButton = $x("//*[text()='Save changes']").as("['Save changes' button]"),
            categoriesContainer = $x("//div[contains(@class,'container') and .//*[.='Categories']]").as("['Categories' items container]"),
            alertNotificationMessage = $("div[class*='MuiAlert-message']").as("[Error message] text"),
            editCategoryNameInput = $("[placeholder='Edit category']").as("['Edit category' input]"),
            submitArchiveCategory = $x("//button[text()='Archive']").as("[Submit 'Archive' button]"),
            submitUnarchiveCategory = $x("//button[text()='Unarchive']").as("[Submit 'Unarchive' button]"),
            avatarImage = $("img[class*='Avatar-img']").as("['Avatar' image]"),
            avatarInput = $("input[type='file']").as("['Avatar' input]");
    private final ElementsCollection allCategories = categoriesContainer.$$x(".//*[./div[contains(@class, 'clickable')]]");
    public ProfilePage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public AppHeader getHeader() {
        return new AppHeader();
    }

    public ProfilePage setName(@Nonnull String name) {
        log.info("Set profile name: {}", name);
        nameInput.shouldBe(visible).setValue(name).pressEnter();
        return this;
    }

    public ProfilePage uploadAvatar(@Nonnull File file) {
        avatarInput.uploadFile(file);
        saveChangesButton.click();
        return this;
    }

    public ProfilePage save() {
        log.info("Save changes");
        saveChangesButton.click();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        log.info("Turn on 'Show archived' categories'");
        showArchivedButton.click();
        showArchivedStatusButton.shouldHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage showOnlyActiveCategories() {
        log.info("Turn off 'Show archived' categories'");
        showArchivedButton.click();
        showArchivedStatusButton.shouldNotHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage addNewCategory(@Nonnull String name) {
        log.info("Add new category with name: {}", name);
        categoryInput.shouldBe(visible).setValue(name).pressEnter();
        return this;
    }

    private SelenideElement getCategoryContainer(@Nonnull String categoryName) {
        return allCategories.findBy(text(categoryName));
    }

    public ProfilePage editCategoryName(@Nonnull String oldCategoryName, @Nonnull String newCategoryName) {
        log.info("Change category name = from [{}], to = [{}]", oldCategoryName, newCategoryName);
        getCategoryContainer(oldCategoryName).$("[aria-label='Edit category']").as("[Category '" + oldCategoryName + " edit button']").click();
        editCategoryNameInput.setValue(newCategoryName).pressEnter();
        return this;
    }

    public ProfilePage setCategoryActive(@Nonnull String categoryName) {
        log.info("Set category active: [{}]", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Unarchive category']")
                .as("[Category '" + categoryName + " unarchive button']").click();
        submitUnarchiveCategory.click();
        return this;
    }

    public ProfilePage setCategoryArchive(@Nonnull String categoryName) {
        log.info("Set category archive: [{}]", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Archive category']")
                .as("[Category '" + categoryName + " archive button']").click();
        submitArchiveCategory.click();
        return this;
    }

    public ProfilePage shouldHaveUsername(@Nonnull String text) {
        log.info("Assert username equals: {}", text);
        usernameInput.shouldHave(value(text));
        return this;
    }

    public ProfilePage shouldHaveName(@Nonnull String name) {
        log.info("Assert name equals: {}", name);
        nameInput.shouldHave(value(name));
        return this;
    }

    public ProfilePage shouldHaveMessageAlert(@Nonnull String alertMessage) {
        log.info("Assert alert has text: {}", alertMessage);
        alertNotificationMessage.shouldBe(visible).shouldHave(text(alertMessage));
        return this;
    }

    public ProfilePage shouldBeArchiveCategory(@Nonnull String categoryName) {
        log.info("Assert name equals: {}", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Unarchive category']")
                .as("[Category '" + categoryName + " unarchive button']").shouldBe(visible);
        return this;
    }

    public ProfilePage shouldBeActiveCategory(@Nonnull String categoryName) {
        log.info("Assert name equals: {}", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Archive category']")
                .as("[Category '" + categoryName + " archive button']").shouldBe(visible);
        return this;
    }

    public ProfilePage shouldCategoryExists(@Nonnull String categoryName) {
        log.info("Assert category with name = [{}] exists", categoryName);
        allCategories.findBy(text(categoryName)).shouldBe(visible);
        return this;
    }

    public ProfilePage shouldHaveImage() {
        log.info("Assert avatar has image");
        avatarImage.shouldBe(visible);
        return this;
    }

    @Override
    public ProfilePage shouldVisiblePageElement() {
        log.info("Assert profile page element are visible");
        usernameInput.shouldBe(visible);
        return this;
    }

    @Override
    public ProfilePage shouldVisiblePageElements() {

        log.info("Assert profile page elements are visible");

        nameInput.shouldBe(visible);
        showArchivedButton.shouldBe(visible);
        saveChangesButton.shouldBe(visible);
        categoryInput.shouldBe(visible);

        return this;

    }

}