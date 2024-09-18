package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement nameInput = $("#name");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final SelenideElement archiveSwitcher = $x("//input[@type='checkbox']");


    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage checkCategoryExists(String category) {
        bubbles.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        archiveSwitcher.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }
}