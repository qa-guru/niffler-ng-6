package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveChangesButton = $("button=[type='submit']");
    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
    private final SelenideElement addNewCategoryInput = $("#category");
    private final ElementsCollection listCategory = $("div").$$("div[role='button']");

    public void checkArchivedCategoryIsDisplay(String categoryName) {
        listCategory.find(text(categoryName)).should(visible);
    }

    public ProfilePage turnOnShowArchivedCategory() {
        showArchivedCheckbox.click();
        return new ProfilePage();
    }
}