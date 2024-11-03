package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final SelenideElement amountInput = $("#amount");
    private  final SelenideElement categoryNameBtn = $(By.xpath("//span[text()='Gregg']"));
    private final Calendar calendar = new Calendar($("input[name='date']"));

    @Step("Редактируем описание траты")
    public void setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        saveBtn.click();
    }

    @Step("Создаем новую трату")
    public void createNewSpending(SpendJson spend) {
        amountInput.setValue(spend.amount().toString());
        categoryNameBtn.click();
        calendar.selectDateInCalendar(spend.spendDate());
        descriptionInput.setValue(spend.description());
        saveBtn.click();
    }


}