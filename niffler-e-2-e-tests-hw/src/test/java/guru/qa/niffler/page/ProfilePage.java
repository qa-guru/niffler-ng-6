package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.CategoryStatusException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.time.Duration;
import java.util.Optional;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
public class ProfilePage {

    private final SelenideElement usernameInput = $("#username").as("['Username' input]"),
            nameInput = $("#name").as("['Name' input]"),
            showArchivedButton = $x("//span[text()='Show archived']").as("['Show archived' checkbox]"),
            showArchivedStatusButton = $("input[type='checkbox']").parent().as("['Show archived' checkbox]"),
            categoryInput = $("[placeholder='Add new category']").as("['Categories search' input]"),
            saveChangesButton = $x("//*[text()='Save changes']").as("['Save changes' button]"),
            categoriesContainer = $x("//div[contains(@class,'container') and .//*[.='Categories']]").as("['Categories' items container]"),
            alertNotificationMessage = $("div[class*='MuiAlert-message']").as("[Error message] text"),
            editCategoryNameInput = $("[placeholder='Edit category']").as("['Edit category' input]"),
            submitArchiveCategory = $x("//button[text()='Archive']").as("[Archive] button]"),
            submitUnarchiveCategory = $x("//button[text()='Unarchive']").as("[Unarchive] button]"),
            avatarImage = $("img[class*='Avatar-img']").as("['Avatar' image]"),
            avatarInput = $("input[type='file']").as("['Avatar' input]");

    private final ElementsCollection allCategories = categoriesContainer.$$x(".//*[./div[contains(@class, 'clickable')]]");


    public AppHeader getHeader() {
        return new AppHeader();
    }

    public String getUsername() {
        return usernameInput.getText();
    }

    public String getName() {
        return nameInput.shouldBe(visible).getValue();
    }

    public ProfilePage setName(@NonNull String name) {
        log.info("Set profile name: {}", name);
        nameInput.shouldBe(visible).setValue(name).pressEnter();
        return this;
    }

    public boolean isShowArchived() {
        var status = showArchivedStatusButton.has(cssClass("Mui-checked"));
        log.info("'Show archived' status: {}", status);
        return status;
    }

    public ProfilePage toggleShowArchived(boolean status) {
        if (status != isShowArchived()) {
            log.info("Change 'Show archived' checkbox status to: {}", status);
            showArchivedButton.click();
        } else {
            log.info("'Show archived' checkbox status is already: {}", status);
        }
        return this;
    }

    public ProfilePage addNewCategory(@NonNull String name) {
        log.info("Add new category with name: {}", name);
        categoryInput.shouldBe(visible).setValue(name).pressEnter();
        isCategoryActive(name, Duration.ofSeconds(2));
        return this;
    }

    public ProfilePage assertAlertMessageHasText(@NonNull String allertMessage) {
        log.info("Assert error message has text: {}", allertMessage);
        alertNotificationMessage.shouldBe(visible).shouldHave(text(allertMessage));
        return this;
    }

    private Optional<SelenideElement> getCategoryContainer(@NonNull String name) {
        categoriesContainer.shouldBe(visible);
        return allCategories.stream().filter(categoryRow -> categoryRow.$x(".//span[contains(@class, 'label')]").has(exactText(name))).findFirst();
    }

    public boolean isCategoryActive(@NonNull String name) {
        return getCategoryContainer(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name = [" + name + "] not found"))
                .$x(".//button[@aria-label='Archive category']").as("[Category = [" + name + "] 'Archive' button]").is(exist);
    }

    public boolean isCategoryActive(@NonNull String name, @NonNull Duration duration) {
        return getCategoryContainer(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name = [" + name + "] not found"))
                .$x(".//button[@aria-label='Archive category']").as("[Category = [" + name + "] 'Archive' button]").is(exist, duration);
    }

    public boolean isCategoryArchived(@NonNull String name) {
        return !isCategoryActive(name);
    }

    public ProfilePage save() {
        saveChangesButton.click();
        return this;
    }

    public ProfilePage changeCategoryName(@NonNull String oldCategoryName, @NonNull String newCategoryName) {

        log.info("Rename category name from [{}] to [{}]", oldCategoryName, newCategoryName);

        SelenideElement category = getCategoryContainer(oldCategoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name = [%s] not found".formatted(oldCategoryName)));

        if (isCategoryArchived(oldCategoryName))
            throw new CategoryStatusException("Category with name = [%s] should be active");

        category.$x(".//button[@aria-label='Edit category']").click();
        editCategoryNameInput.setValue(newCategoryName).pressEnter();
        return this;

    }

    /**
     * Changing category status:<br/> true - active,<br/> false - archived
     */
    public ProfilePage changeCategoryStatus(@NonNull String categoryName, boolean status) {

        SelenideElement category = getCategoryContainer(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name = [%s] not found".formatted(categoryName)));

        if (status != isCategoryActive(categoryName)) {
            log.info("Change category archived status to: [{}]", status);
            category.$x(".//button[@aria-label='" + (status ? "Unarchive category" : "Archive category") + "']").click();
            (status ? submitUnarchiveCategory : submitArchiveCategory).click();
        } else {
            log.info("Category [{}] is already {}", categoryName, status ? "active" : "archived");
        }

        return this;

    }

    public ProfilePage assertCategoryHasStatus(@NonNull String categoryName, boolean status) {
        log.info("Assert category [{}] archived status = [{}]", categoryName, status);
        var categoryContainer = getCategoryContainer(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category with name = [%s] not found".formatted(categoryName)));
        categoryContainer.$x(".//button[@aria-label='" + (status ? "Archive category" : "Unarchive category") + "']")
                .is(exist, Duration.ofSeconds(1));
        return this;
    }

    public ProfilePage uploadAvatar(@NonNull File file) {
        avatarInput.uploadFile(file);
        saveChangesButton.click();
        return this;
    }

    public ProfilePage assertCategoryExists(@NonNull String categoryName) {
        log.info("Assert category with name = [{}] exists", categoryName);
        Assertions.assertNotNull(getCategoryContainer(categoryName).orElse(null));
        return this;
    }

    public ProfilePage assertAvatarHasImage() {
        log.info("Assert avatar has image");
        avatarImage.shouldBe(visible);
        return this;
    }

}
