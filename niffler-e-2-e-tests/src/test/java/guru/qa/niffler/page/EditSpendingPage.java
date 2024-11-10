package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");

  private final Calendar calendar = new Calendar($(".SpendingCalendar"));

  @Nonnull
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  public void save() {
    saveBtn.click();
  }
}
