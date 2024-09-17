package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement showArchiveSwitcher = $(".PrivateSwitchBase-input");
    private final ElementsCollection categoryNames = $$(".MuiChip-label");

    public boolean isCategoryActive(String category) {
        boolean result = false;
        SelenideElement firstParentElement = $$(".MuiChip-label").findBy(Condition.text(category)).parent();
        SelenideElement elementToCheckArchive = firstParentElement.parent().$("[aria-label='Unarchive category']");
        SelenideElement elementToCheckActive = firstParentElement.parent().$(".MuiBox-root");
        String firstParentElementClass = firstParentElement.getAttribute("class");
        if (firstParentElementClass.contains("MuiChip-filledPrimary") && elementToCheckActive.exists()) {
            result = true;
        } else if (firstParentElementClass.contains("MuiChip-filledDefault") && elementToCheckArchive.exists()) {
            result = false;
        }
        return result;
    }

    public ProfilePage switchArchiveSwitcher(boolean showArchive) {
        if (showArchive) {
            if (!showArchiveSwitcher.is(Condition.checked)) {
                showArchiveSwitcher.click();
            }
        } else if(!showArchive){
            if (showArchiveSwitcher.is(Condition.checked)) {
                showArchiveSwitcher.click();
            }
        }
        return this;
    }
}
