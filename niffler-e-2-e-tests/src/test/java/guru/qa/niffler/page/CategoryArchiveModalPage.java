package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CategoryArchiveModalPage {

    private final SelenideElement archiveCategoryModal = $(".MuiDialog-paper");
    private final SelenideElement archiveCategoryModalText = archiveCategoryModal.$("#alert-dialog-slide-description");
    private final SelenideElement archiveCategoryModalCloseButton = archiveCategoryModal.$("button:contains('Close')");
    private final SelenideElement archiveCategoryModalArchiveButton = archiveCategoryModal.$("button:contains('Archive')");

    public CategoryArchiveModalPage(String categoryName) {
        archiveCategoryModal.shouldBe(Condition.visible);
        archiveCategoryModalText.shouldHave(Condition.text("Do you really want to archive " + categoryName + "? After this change it won't be available while creating spends"));
        archiveCategoryModalCloseButton.shouldBe(Condition.visible).shouldHave(Condition.text("Close"));
        archiveCategoryModalArchiveButton.shouldBe(Condition.visible).shouldHave(Condition.text("Archive"));
    }

    public MainPage clickCancelButton() {
        archiveCategoryModalCloseButton.click();
        return new MainPage();
    }

    public MainPage clickArchiveButton() {
        archiveCategoryModalArchiveButton.click();
        return new MainPage();
    }






}
