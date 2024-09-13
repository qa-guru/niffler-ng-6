package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement
            imageUpload = $(".image__input-label"),
            saveChangesButton = $(":r1:"),
            inputName = $("#name"),
            showArchivedCheckBox = $("[type='checkbox']"),
            inputCategory = $("#category"),
            alertSuccessUpdate = $("[role='alert']"),
            closeAlert = $("[data-testid='CloseIcon']"),
            closeOrArchiveCategoryOrUnarchive = $(".MuiDialogActions-spacing");

    public ProfilePage setName(String name) {
        inputName.setValue(name);

        return new ProfilePage();
    }

    public ProfilePage setCategory(String category) {
        inputCategory.setValue(category);

        return new ProfilePage();
    }

    public ProfilePage clickOnCheckboxShowArchived() {
        showArchivedCheckBox.click();

        return new ProfilePage();
    }

    public ProfilePage clickOnSaveChangesButton() {
        saveChangesButton.click();

        return new ProfilePage();
    }

    public void uploadImage() {
        imageUpload.click();
    }

    public void checkAlertSuccessfulUpdateAndCloseAlert() {
        alertSuccessUpdate.shouldHave(text("Profile successfully updated"));
        closeAlert.click();
    }

    public ProfilePage clickCloseOrArchiveOrUnarchiveCategory(String closeOrArchiveOrUnarchive) {
        closeOrArchiveCategoryOrUnarchive.$(byText(closeOrArchiveOrUnarchive)).click();

        return new ProfilePage();
    }

    public ProfilePage checkCategoryByNameInProfile(String nameCategory) {
        $(".MuiGrid-root.MuiGrid-container.MuiGrid-spacing-xs-2.css-3w20vr")
                .shouldHave(text(nameCategory));

        return new ProfilePage();
    }

    public ProfilePage checkNotCategoryByNameInProfile(String nameCategory) {
        $(".MuiGrid-root.MuiGrid-container.MuiGrid-spacing-xs-2.css-3w20vr")
                .shouldNotHave(text(nameCategory));

        return new ProfilePage();
    }

    public ProfilePage clickArchiveCategory(String name) {
        $$("[class='MuiBox-root css-1lekzkb']")
                .filter(text(name))
                .first()
                .$("button[aria-label='Archive category']")
                .click();

        return new ProfilePage();
    }

    public ProfilePage clickUnarchiveCategory(String name) {
        $$("[class='MuiBox-root css-1lekzkb']")
                .filter(text(name))
                .first()
                .$("[data-testid='UnarchiveOutlinedIcon']")
                .click();

        return new ProfilePage();
    }
}
