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
  public StatComponent checkBubbles(Color... expectedColors) {
    bubbles.should(color(expectedColors));
    return this;
  }

  @Step("Check that stat bubbles are visible {expectedColors}")
  @Nonnull
  public StatComponent checkBubblesInExactOrder(Bubble... expectedColors) {
    bubbles.should(statBubbles(expectedColors));
    return this;
  }

  @Step("Check that stat bubbles are visible {expectedColors}")
  @Nonnull
  public StatComponent checkBubblesInAnyOrder(Bubble... expectedColors) {
    bubbles.should(statBubblesInAnyOrder(expectedColors));
    return this;
  }

  @Step("Check that stat bubbles are visible {expectedColors}")
  @Nonnull
  public StatComponent checkBubblesContains(Bubble... expectedColors) {
    bubbles.should(statBubblesContains(expectedColors));
    return this;
  }
}
