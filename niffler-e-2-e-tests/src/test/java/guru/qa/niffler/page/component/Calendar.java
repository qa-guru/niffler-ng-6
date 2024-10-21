package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Calendar {
  private final SelenideElement self;

  public Calendar(SelenideElement self) {
    this.self = self;
  }
}
