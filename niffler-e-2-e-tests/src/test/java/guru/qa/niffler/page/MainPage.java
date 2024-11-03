package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl() + "main";
    private final SelenideElement statImage = $("canvas[role='img']");

    protected final Header header = new Header();
    protected final SpendingTable spendingTable = new SpendingTable();
    protected final StatComponent statComponent = new StatComponent();

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Check that page is loaded")
    @Override
    @Nonnull
    public MainPage checkThatPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler"));
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
        return this;
    }

    public MainPage checkStat(BufferedImage expectedImage) {
        BufferedImage actual = null;
        try {
            Thread.sleep(3000);
            actual = ImageIO.read(statImage.screenshot());
        } catch (IOException  | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertFalse(new ScreenDiffResult(
                expectedImage,
                actual
        ));
        return this;
    }
}
