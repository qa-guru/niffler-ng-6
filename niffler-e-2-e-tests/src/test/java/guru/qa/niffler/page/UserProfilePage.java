package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class UserProfilePage extends GlobalTemplatePage {

    private final SelenideElement userAvatar = $(".MuiAvatar-root");
    private final SelenideElement usernameField = $("#username");
    private final SelenideElement nameField = $("#name");
    private final SelenideElement saveChangesButton = $("#\\:rb\\:");
    private final SelenideElement uploadButton = $("label[for='image__input']");
    private final SelenideElement categoryInputField = $("#category");
    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
    private final ElementsCollection categories = $$("span.MuiChip-label");

    public UserProfilePage setName(String name) {
        nameField.setValue(name);
        clickSaveChangesButton();
        return this;
    }

    public UserProfilePage clickUploadNewPictureButton() {
        uploadButton.click();
        return this;
    }

    public UserProfilePage addCategory(String category) {
        categoryInputField.setValue(category);
        categoryInputField.pressEnter();
        return this;
    }

    public UserProfilePage toggleShowArchived(boolean shouldBeChecked) {
        if (showArchivedCheckbox.isSelected() != shouldBeChecked) {
            showArchivedCheckbox.click();
        }
        return this;
    }

    public void clickSaveChangesButton() {
        saveChangesButton.click();
    }

    public void clickCategoryNameLabel(String categoryName) {
        categories.find(Condition.text(categoryName)).click();
    }

    public void clickCategoryNameEditIcon(String categoryName) {
        categories.find(Condition.text(categoryName))
                .closest("div") // Gets the closest div element containing both the label and the edit button
                .find("button[aria-label='Edit category']") // Finds the edit button by its aria-label
                .click(); // Click on the edit button
    }

    public CategoryArchiveModalPage clickArchiveCategory(String categoryName) {
        // Find the category element by text and then navigate to the archive button
        categories.find(Condition.text(categoryName))
                .closest("div") // Gets the closest div element containing both the label and the archive button
                .find("button[aria-label='Archive category']") // Finds the archive button by its aria-label
                .click(); // Click on the archive button
        return new CategoryArchiveModalPage(categoryName);
    }

    public UserProfilePage checkThatCategoryIsPresentInCategoriesList(String categoryName) {
        categories.find(Condition.text(categoryName)).shouldBe(Condition.visible);
        return this;
    }

    public UserProfilePage checkThatCategoryIsNotPresentInCategoriesList(String categoryName) {
        categories.find(Condition.text(categoryName)).shouldNotBe(Condition.visible);
        return this;
    }
}


