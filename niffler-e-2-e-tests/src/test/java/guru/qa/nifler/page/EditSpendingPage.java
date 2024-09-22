package guru.qa.nifler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement submitButton = $("#save");

  public void editSpendingDescription(String description) {
    descriptionInput.setValue(description);
    submitButton.click();
  }
}
