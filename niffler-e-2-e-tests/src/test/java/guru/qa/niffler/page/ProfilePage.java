package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement categoryInput = $("input[name=category]");
    private final SelenideElement showArchivedButton = $(".css-1m9pwf3[type='checkbox']");
    private final ElementsCollection activeCategoryList = $$("div[tabindex='0'].MuiChip-root");
    private final ElementsCollection archiveCategoryList = $$("div[tabindex='-1'].MuiChip-root");
    private final SelenideElement archiveButton = $(By.xpath("//button[contains(text(), 'Archive')]"));
    private final SelenideElement unarchivedButton = $(By.xpath("//button[contains(text(), 'Unarchive')]"));
    private final SelenideElement messageAfterArchivedUnarchivedCategory = $(".MuiAlert-message");

    public ProfilePage addNewCategory(String categoryName) {
        categoryInput.setValue(categoryName)
                .pressEnter();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        showArchivedButton.scrollIntoView(false);
        showArchivedButton.click();
        return this;
    }

    public ProfilePage showUnarchivedCatogories() {
        showArchivedButton.scrollIntoView(false);
        showArchivedButton.click();
        return this;
    }

    public void shouldHaveActiveCategoryByName(String categoryName) {
        activeCategoryList.findBy(text(categoryName))
                .shouldBe(visible);
    }

    public void shouldHaveArchiveCategoryByName(String categoryName) {
        archiveCategoryList.findBy(text(categoryName))
                .shouldBe(visible);
    }

    public ProfilePage shouldHaveMessageAfterArchivedCategory(String categoryName) {
        final String successMessage = String.format("Category %s is archived", categoryName);
        messageAfterArchivedUnarchivedCategory.shouldHave(text(successMessage))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage shouldHaveMessageAfterUnarchivedCategory(String categoryName) {
        final String successMessage = String.format("Category %s is unarchived", categoryName);
        messageAfterArchivedUnarchivedCategory.shouldHave(text(successMessage))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage archivedCategoryByName(String categoryName) {
        String archivedCategoryLocator = ".css-dxoo7k[aria-label='Archive category']";
        activeCategoryList.findBy(text(categoryName))
                .parent()
                .$(archivedCategoryLocator)
                .click();
        archiveButton.click();
        return this;
    }

    public ProfilePage unarchivedCategoryByName(String name) {
        String unarchivedCategoryLocator = ".css-dxoo7k[aria-label='Unarchive category']";
        archiveCategoryList.findBy(text(name))
                .parent()
                .$(unarchivedCategoryLocator)
                .click();
        return this;
    }


    public ProfilePage clickArchiveButton() {
        archiveButton.click();
        return this;
    }

    public ProfilePage clickUnarchivedButton() {
        unarchivedButton.click();
        return this;
    }
}
