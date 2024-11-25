package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class SelectField extends BaseComponent<SelectField> {

  public SelectField(SelenideElement self) {
    super(self);
  }

  private final SelenideElement input = self.$("input");

  public void setValue(String value) {
    self.click();
    $$("li[role='option']").find(text(value)).click();
  }

  @Step("Check that selected value is equal to {value}")
  public void checkSelectValueIsEqualTo(String value) {
    self.shouldHave(text(value));
  }
}
