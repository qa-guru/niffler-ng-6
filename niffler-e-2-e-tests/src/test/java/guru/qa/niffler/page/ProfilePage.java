package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement showArchivedCategoriesCheckbox = $("input[type='checkbox']");

    public ProfilePage setArvedCategoriesVisible() {
        showArchivedCategoriesCheckbox.click();
        return this;
    }

    private final ElementsCollection categoriesList = $$(".MuiChip-filled");

    public void categoriesShouldBeVisible(String category) {
        categoriesList.findBy(text(category)).shouldBe(visible);
    }
}
