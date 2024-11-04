package guru.qa.niffler.page.page;

import lombok.NoArgsConstructor;

import javax.annotation.ParametersAreNonnullByDefault;

@NoArgsConstructor
@ParametersAreNonnullByDefault
public abstract class BasePage<T> {

    protected BasePage(boolean assertPageElementsOnStart) {
        if (assertPageElementsOnStart)
            try {
                shouldVisiblePageElements();
            } catch (Exception ignored) {
                // NOP
            }
    }

    public abstract T shouldVisiblePageElement();

    public abstract T shouldVisiblePageElements();

}