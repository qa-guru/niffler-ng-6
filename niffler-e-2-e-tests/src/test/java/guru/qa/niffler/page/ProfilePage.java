package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage extends BasePage<ProfilePage> {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement showArchivedCheckbox = $("input[type='checkbox']");
    private final ElementsCollection listCategory = $("div").$$("div[role='button']");
    private final ElementsCollection archiveSubmitButtons = $$("div[role='dialog'] button");
    private final SelenideElement uploadAvatarButton = $("#image__input");
    public final SelenideElement avatarImage = $("main img");

    @Step("Проверяем, что отображаются архивные категории")
    public void checkArchivedCategoryIsDisplay(String categoryName) {
        listCategory.find(text(categoryName)).should(visible);
    }

    @Step("Ставим чекбокс отображать активные категории")
    public ProfilePage turnOnShowArchivedCategory() {
        showArchivedCheckbox.click();
        return this;
    }

    @Step("Меняем имя категории")
    public void changeName(String name) {
        nameInput.setValue(name);
        saveChangesButton.click();
        nameInput.shouldNotBe(empty);
    }

    @Step("Загружаем аватар пользователя")
    public ProfilePage uploadAvatar(String file){
        uploadAvatarButton.uploadFromClasspath(file);
        saveChangesButton.click();
        return this;
    }

    @Step("Архивируем категорию")
    public ProfilePage archivedCategory(String categoryName) {
        listCategory.find(text(categoryName)).parent().parent().$$("div").get(2).$$("button").get(1).click();
        archiveSubmitButtons.find(text("Archive")).click();
        return this;
    }

    @Step("Возвращаемся на главную страницу")
    public MainPage returnToMainPage() {
        return  header.toMainPage();
    }

    @Step("Get screenshot of avatar picture")
    public BufferedImage avatarScreenshot() throws IOException {
        return ImageIO.read(new ProfilePage().avatarImage.screenshot());
    }



}