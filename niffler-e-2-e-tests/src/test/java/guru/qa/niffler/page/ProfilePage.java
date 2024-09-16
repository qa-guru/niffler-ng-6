package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement archiveButtonSubmit = $x("//button[text()='Archive']");
    private final SelenideElement unarchiveButtonSubmit = $x("//button[text()='Unarchive']");
    private final ElementsCollection categoryList = $$(".MuiChip-root");
    private final SelenideElement successArchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement successUnarchiveMessage = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    private final SelenideElement showArchiveCategoryButton = $x("//input[@type='checkbox']");

    public ProfilePage clickArchiveButtonForCategoryName(String categoryName) {
        // Находим элемент с текстом категории и кликаем по кнопке "Архивировать"
        SelenideElement archiveButtonInRow = categoryList.stream()
                .filter(category -> category.text().equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName))
                .parent().$(".MuiIconButton-sizeMedium[aria-label='Archive category']");
        archiveButtonInRow.click();
        return this;
    }

    public ProfilePage clickUnarchiveButtonForCategoryName(String categoryName) {
        // Находим элемент с текстом категории и кликаем по кнопке "Разархивировать"
        SelenideElement unarchiveButtonInRow = categoryList.stream()
                .filter(category -> category.text().equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName))
                .parent().$("[data-testid='UnarchiveOutlinedIcon']");
        unarchiveButtonInRow.click();
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