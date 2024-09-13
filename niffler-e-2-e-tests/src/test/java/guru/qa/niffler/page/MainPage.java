package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    private final SelenideElement
            statisticText = $("#stat"),
            spendingsText = $("#spendings"),
            personIcon = $("[data-testid='PersonIcon']"),
            personMenu = $("[role='menu']"),
            imageInput = $(".image__input-label");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();

        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkSuccessfulLogin() {
        statisticText.shouldBe(visible).shouldHave(text("Statistics"));
        spendingsText.shouldBe(visible).shouldHave(text("History of Spendings"));
    }

    public ProfilePage clickToProfileUser(){
        personIcon.click();
        personMenu.shouldBe(visible).$(byText("Profile")).click();
        imageInput.shouldBe(visible);

        return new ProfilePage();
    }

    public LoginPage clickToSignOut() {
        personIcon.click();
        personMenu.shouldBe(visible).$(byText("Sign out")).click();

        return new LoginPage();
    }
}
