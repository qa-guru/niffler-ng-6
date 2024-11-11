package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.*;
import static java.util.Objects.requireNonNull;

public class StatComponent extends BaseComponent<StatComponent> {
    public StatComponent() {
        super($("#stat"));
    }
    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
    private final SelenideElement chart = $("canvas[role='img']");
    @Step("Get screenshot of stat chart")
    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(requireNonNull(chart.screenshot()));
    }

    @Step("Check that stat bubbles contains colors {expectedColors}")
    @Nonnull
    public StatComponent checkBubblesColor(Color... expectedColors) {
        bubbles.should(color(expectedColors));
        return this;
    }

    @Step("Check that stat bubbles contains colors and text {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubbles(Bubble... expectedBubbles) {
        bubbles.should(statBubbles(expectedBubbles));
        return this;
    }

    @Step("Check that stat bubbles contains colors and text {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubblesInAnyOrder(Bubble... expectedBubbles) {
        bubbles.should(statBubblesInAnyOrder(expectedBubbles));
        return this;
    }

    @Step("Check that stat bubbles contains colors and text among other bubbles {expectedBubbles}")
    @Nonnull
    public StatComponent checkBubblesContains(Bubble... expectedBubbles) {
        bubbles.should(statBubblesContains(expectedBubbles));
        return this;
    }
}