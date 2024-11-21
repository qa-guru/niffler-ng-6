package guru.qa.niffler.page.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.conditions.ScreenshotCondition.screenshot;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

    private static final String PATH_TO_RESOURCES = "niffler-e-2-e-tests-hw/src/test/resources/";

    private final SelenideElement usernameInput = $("#username").as("['Username' input]"),
            nameInput = $("#name").as("['Name' input]"),
            showArchivedButton = $x("//span[text()='Show archived']").as("['Show archived' checkbox]"),
            showArchivedStatusButton = $("input[type='checkbox']").parent().as("['Show archived' checkbox]"),
            categoryInput = $("[placeholder='Add new category']").as("['Categories search' input]"),
            saveChangesButton = $x("//*[text()='Save changes']").as("['Save changes' button]"),
            categoriesContainer = $x("//div[contains(@class,'container') and .//*[.='Categories']]").as("['Categories' items container]"),
            editCategoryNameInput = $("[placeholder='Edit category']").as("['Edit category' input]"),
            submitArchiveCategory = $x("//button[text()='Archive']").as("[Submit 'Archive' button]"),
            submitUnarchiveCategory = $x("//button[text()='Unarchive']").as("[Submit 'Unarchive' button]"),
            avatarImage = $("main img[class*='Avatar-img']").as("['Avatar' image]"),
            avatarInput = $("main input[type='file']").as("['Avatar' input]");
    private final ElementsCollection allCategories = categoriesContainer.$$x(".//*[./div[contains(@class, 'clickable')]]");

    public ProfilePage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public Header getHeader() {
        return new Header();
    }

    public ProfilePage setName(String name) {
        log.info("Set profile name: {}", name);
        nameInput.shouldBe(visible).setValue(name).pressEnter();
        return this;
    }

    public ProfilePage uploadAvatar(String pathToFile) {
        avatarInput.uploadFromClasspath(pathToFile);
        saveChangesButton.shouldBe(clickable).click();
        return this;
    }

    public ProfilePage save() {
        log.info("Save changes");
        saveChangesButton.shouldBe(clickable).click();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        log.info("Turn on 'Show archived' categories'");
        showArchivedButton.shouldBe(clickable).click();
        showArchivedStatusButton.shouldHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage showOnlyActiveCategories() {
        log.info("Turn off 'Show archived' categories'");
        showArchivedButton.shouldBe(clickable).click();
        showArchivedStatusButton.shouldNotHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage addNewCategory(String name) {
        log.info("Add new category with name: {}", name);
        categoryInput.shouldBe(visible).setValue(name).pressEnter();
        return this;
    }

    private SelenideElement getCategoryContainer(String categoryName) {
        return allCategories.findBy(text(categoryName));
    }

    public ProfilePage editCategoryName(String oldCategoryName, String newCategoryName) {
        log.info("Change category name = from [{}], to = [{}]", oldCategoryName, newCategoryName);
        getCategoryContainer(oldCategoryName).$("[aria-label='Edit category']").as("[Category '" + oldCategoryName + " edit button']").shouldBe(clickable).click();
        editCategoryNameInput.setValue(newCategoryName).pressEnter();
        return this;
    }

    public ProfilePage setCategoryActive(String categoryName) {
        log.info("Set category active: [{}]", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Unarchive category']")
                .as("[Category '" + categoryName + " unarchive button']").shouldBe(clickable).click();
        submitUnarchiveCategory.shouldBe(clickable).click();
        return this;
    }

    public ProfilePage setCategoryArchive(String categoryName) {
        log.info("Set category archive: [{}]", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Archive category']")
                .as("[Category '" + categoryName + " archive button']").shouldBe(clickable).click();
        submitArchiveCategory.shouldBe(clickable).click();
        return this;
    }

    public ProfilePage shouldHaveUsername(String text) {
        log.info("Assert username equals: {}", text);
        usernameInput.shouldHave(value(text));
        return this;
    }

    public ProfilePage shouldHaveName(String name) {
        log.info("Assert name equals: {}", name);
        nameInput.shouldHave(value(name));
        return this;
    }

    public ProfilePage shouldBeArchiveCategory(String categoryName) {
        log.info("Assert name equals: {}", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Unarchive category']")
                .as("[Category '" + categoryName + " unarchive button']").shouldBe(visible);
        return this;
    }

    public ProfilePage shouldBeActiveCategory(String categoryName) {
        log.info("Assert name equals: {}", categoryName);
        getCategoryContainer(categoryName).$x(".//button[@aria-label='Archive category']")
                .as("[Category '" + categoryName + " archive button']").shouldBe(visible);
        return this;
    }

    public ProfilePage shouldCategoryExists(String categoryName) {
        log.info("Assert category with name = [{}] exists", categoryName);
        allCategories.findBy(text(categoryName)).shouldBe(visible);
        return this;
    }

    @Deprecated
    public ProfilePage shouldHaveImage() {
        log.info("Assert avatar has image");
        avatarImage.shouldBe(visible);
        return this;
    }

    /**
     * @param urlToScreenshot - Example: "/files/img/spend/expected/img_1.png";
     */
    @Step("Screenshot of expected and actual avatar should equals: {urlToScreenshot}")
    public ProfilePage shouldHaveScreenshotAvatar(
            String urlToScreenshot,
            double percentOfTolerance,
            int millis,
            boolean rewriteExpected
    ) {
        log.info("Should have expected avatar screenshot");
        avatarImage.shouldHave(screenshot(urlToScreenshot, percentOfTolerance, millis, rewriteExpected));
        return this;
    }

    /**
     * @param urlToScreenshot - Example: "/files/img/avatar/expected/img_1.png";
     */
    public ProfilePage shouldHaveScreenshotAvatar(String urlToScreenshot, int millis) {
        return shouldHaveScreenshotAvatar(urlToScreenshot, 0.005, millis, false);
    }

    /**
     * @param urlToScreenshot - Example: "/files/img/avatar/expected/img_1.png";
     */
    public ProfilePage shouldHaveScreenshotAvatar(String urlToScreenshot) {
        return shouldHaveScreenshotAvatar(urlToScreenshot, 0.005, 3000, false);
    }

    /**
     * @param urlToScreenshot - Example: "/files/img/avatar/expected/img_1.png";
     */
    public ProfilePage shouldHaveScreenshotAvatar(String urlToScreenshot, boolean rewriteExpected) {
        return shouldHaveScreenshotAvatar(urlToScreenshot, 0.005, 3000, rewriteExpected);
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