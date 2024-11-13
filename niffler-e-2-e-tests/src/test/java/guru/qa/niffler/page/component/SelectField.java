package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;

public class SelectField extends BaseComponent<SelectField> {

  private final SelenideDriver driver;

  public SelectField(SelenideElement self, SelenideDriver driver) {
    super(self);

    this.driver = driver;
    this.input = self.$("input");
  }

  private final SelenideElement input;

  public void setValue(String value) {
    self.click();
    driver.$$("li[role='option']").find(text(value)).click();
  }

  @Step("Check that selected value is equal to {value}")
  public void checkSelectValueIsEqualTo(String value) {
    self.shouldHave(text(value));
  }
}
