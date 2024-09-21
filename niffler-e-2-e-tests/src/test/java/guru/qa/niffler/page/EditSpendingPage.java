package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");

    @Step("Установить новое описание траты")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Кликнуть на кнопку сохранения")
    public EditSpendingPage save() {
        saveBtn.click();
        return this;
    }
}
