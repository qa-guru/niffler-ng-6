package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.io.File;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage extends BasePage<EditSpendingPage> {
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement categoryNameBtn = $(By.xpath("//span[text()='Gregg']"));
    private final SelenideElement amount = $("#amount");
    private final Calendar calendar = new Calendar($("input[name='date']"));

    @Step("Редактируем описание траты")
    public MainPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        saveBtn.click();
        return new MainPage();
    }

    @Step("Редактируем сумму траты")
    public MainPage setNewSpendingAmount(String amount) {
        amountInput.clear();
        amountInput.setValue(amount);
        saveBtn.click();
        return new MainPage();
    }

    @Step("Создаем новую трату")
    public MainPage createNewSpending(SpendJson spend) {
        amountInput.setValue(spend.amount().toString());
        categoryNameBtn.click();
        calendar.selectDateInCalendar(spend.spendDate());
        descriptionInput.setValue(spend.description());
        saveBtn.click();
        return new MainPage();
    }
}