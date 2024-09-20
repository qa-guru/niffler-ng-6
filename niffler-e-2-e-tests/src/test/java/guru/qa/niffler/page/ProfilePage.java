package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {

    private final SelenideElement buttonUploadPicture = $("label.image__input-label");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement buttonSaveName = $("button[type='submit']");
    private final SelenideElement inputNewCategory = $("input[placeholder='Add new category']");
    private final SelenideElement successShowUpdateProfile = $("div.MuiAlert-message");
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement unarchiveButtonSubmit = $x("//button[text()='Unarchive']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement successArchiveMessage = $("div.MuiAlert-message");
    private final SelenideElement successUnarchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement showArchiveCategoryButton = $x("//input[@type='checkbox']");


    public ProfilePage editName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage shouldSuccessProfileUpdated(String message) {
        successShowUpdateProfile.shouldHave(text(message));
        return this;
    }

    public ProfilePage saveName(){
        buttonSaveName.click();
        return this;
    }

    public ProfilePage clickArchiveButtonForCategoryName(String categoryName) {
        // Проходим по списку категорий
        categoryList.findBy(text(categoryName)).sibling(0).$("button[aria-label='Archive category']").click();
        return this;
    }

    public ProfilePage clickUnarchiveButtonForCategoryName(String categoryName) {
        categoryList.findBy(text(categoryName)).sibling(0).$("button[aria-label='Unarchive category']").click();
        return this;
    }

    public ProfilePage clickShowArchiveCategoryButton() {
        Selenide.executeJavaScript("arguments[0].scrollIntoView(true);", showArchiveCategoryButton);
        Selenide.executeJavaScript("arguments[0].click();", showArchiveCategoryButton);
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
        successUnarchiveMessage.shouldHave(text("Category " + value + " is unarchived")).shouldBe(visible);
        return this;
    }

    // Метод для проверки видимости активной категории
    public ProfilePage shouldVisibleActiveCategory(String value) {
        categoryList.findBy(text(value)).shouldBe(visible);
        return this;
    }

    // Метод для проверки, что архивная категория не видна
    public ProfilePage shouldNotVisibleArchiveCategory(String value) {
        categoryList.findBy(text(value)).shouldNotBe(visible);
        return this;
    }

}
