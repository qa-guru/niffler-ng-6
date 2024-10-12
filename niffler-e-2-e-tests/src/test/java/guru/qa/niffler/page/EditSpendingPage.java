package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement submitButton = $("#save");

  @Step("Редактировать описание траты")
  public void editSpendingDescription(String description) {
    descriptionInput.setValue(description);
    submitButton.click();
  }

  @Step("Проверка наличия категории")
  public EditSpendingPage checkCategoryIsPresent(String categoryName) {
    $x("//span[text()='" + categoryName + "']").as("Категория " + categoryName).shouldBe(visible);
    return this;
  }

  @Step("Проверка отсутствия категории")
  public EditSpendingPage checkCategoryIsNotPresent(String categoryName) {
    $x("//span[text()='" + categoryName + "']").as("Категория " + categoryName).shouldNotBe(visible);
    return this;
  }
}
