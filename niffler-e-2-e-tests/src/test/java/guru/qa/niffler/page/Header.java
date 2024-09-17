package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header {
    private final SelenideElement profileImageButton = $("[aria-label=\"Menu\"]");
    private final ElementsCollection profilePopUpMenuButtons = $$("[role=\"menu\"] .MuiButtonBase-root");

    public void clickProfileMenuButton(String buttonName){
        profileImageButton.click();
        profilePopUpMenuButtons.findBy(Condition.text(buttonName)).click();
    }
}