package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
    private final SelenideElement addNewCategoryInput = $("#category");
    private final ElementsCollection listCategory = $("div").$$("div[role='button']");

    @Step("Проверяем, что отображаются архивные категории")
    public void checkArchivedCategoryIsDisplay(String categoryName) {
        listCategory.find(text(categoryName)).should(visible);
    }

    @Step("Ставим чекбокс отображать активные категории")
    public ProfilePage turnOnShowArchivedCategory() {
        showArchivedCheckbox.click();
        return this;
    }

    @Step("Меняем имя категории")
    public void changeName(String name) {
        nameInput.setValue(name);
        saveChangesButton.click();
        nameInput.shouldNotBe(empty);
    }
}