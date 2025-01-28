package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.api.UserdataApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.converter.ToDriverArgumentConverter;
import guru.qa.niffler.jupiter.extension.NonStaticBrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideConfigBrowser;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;



public class LoginWebThreadTest {
    private static final Config CFG = Config.getInstance();
    private UserdataApiClient userdataApiClient = new UserdataApiClient();

    @RegisterExtension
    private static final NonStaticBrowserExtension nonStaticBrowserExtension = new NonStaticBrowserExtension();


    @ParameterizedTest
    @EnumSource(SelenideConfigBrowser.class)
    void testManyBrowser(@ConvertWith(ToDriverArgumentConverter.class) SelenideDriver driver) {
        nonStaticBrowserExtension.drivers().add(driver);
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginIncorrect("esa1", "12345")
                .checkButtonSingInIsDisplayed();
    }


    @ParameterizedTest
    @EnumSource(SelenideConfigBrowser.class)
    void testManyBrowser1(@ConvertWith(ToDriverArgumentConverter.class) SelenideDriver driver) {
        nonStaticBrowserExtension.drivers().add(driver);
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginIncorrect("esa1", "12345")
                .checkButtonSingInIsDisplayed();
    }

}