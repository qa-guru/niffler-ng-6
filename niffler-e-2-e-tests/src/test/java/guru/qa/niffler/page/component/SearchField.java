package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.not;

public class SearchField extends BaseComponent<SearchField> {
  public SearchField(@Nonnull SelenideElement self, SelenideDriver driver) {
    super(self);

    this.clearSearchInputBtn = driver.$("#input-clear");
  }

  public SearchField(SelenideDriver driver) {
    super(driver.$("input[aria-label='search']"));
    this.clearSearchInputBtn = driver.$("#input-clear");
  }

  private final SelenideElement clearSearchInputBtn;

  @Step("Perform search for query {query}")
  @Nonnull
  public SearchField search(String query) {
    clearIfNotEmpty();
    self.setValue(query).pressEnter();
    return this;
  }

  @Step("Try to clear search field")
  @Nonnull
  public SearchField clearIfNotEmpty() {
    if (self.is(not(empty))) {
      clearSearchInputBtn.click();
      self.should(empty);
    }
    return this;
  }
}
