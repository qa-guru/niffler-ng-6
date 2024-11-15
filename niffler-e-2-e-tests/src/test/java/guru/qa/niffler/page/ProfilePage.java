package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfilePage {

    private final SelenideElement iconShowArchived = $("[type='checkbox']");
    private final String archiveCategoryButton = "button[aria-label='Archive category']";
    private final String activeCategoryButton = "button[aria-label='Unarchive category']";
    private final SelenideElement confirmArchiveButton = $x(".//button[text()='Archive']");
    private final SelenideElement confirmActiveButton = $x(".//button[text()='Unarchive']");

    public void clickIconShowArchived() {
        iconShowArchived.click();
    }

    public ElementsCollection findCategory(String category) {
        ElementsCollection elements = $$(".MuiBox-root").filterBy(Condition.text(category));
        elements.shouldBe(sizeGreaterThan(0), Duration.ofSeconds(5));
        if (elements.isEmpty()) {
            throw new AssertionError("Category with name '" + category + "' not found");
        }
        return elements;
    }

    public void clickArchiveButton(String category) {
        ElementsCollection elements = findCategory(category);
        elements.first().$(archiveCategoryButton).click();
    }

    public void clickActiveButton(String category) {
        ElementsCollection elements = findCategory(category);
        elements.first().$(activeCategoryButton).click();
    }

    public void confirmArchivation() {
        confirmArchiveButton.click();
    }

    public void confirmActivation() {
        confirmActiveButton.click();
    }

    public void checkThatCategoryIsActive(String category) {
        ElementsCollection elements = findCategory(category);
        assertTrue(elements.first().$(archiveCategoryButton).exists(), category + " is archived");
    }

    public void checkThatCategoryIsArchived(String category) {
        ElementsCollection elements = findCategory(category);
        assertTrue(elements.first().$(activeCategoryButton).exists(), category + "is active");
    }
}
