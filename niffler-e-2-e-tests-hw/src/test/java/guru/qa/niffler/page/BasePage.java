package guru.qa.niffler.page;

import lombok.NoArgsConstructor;

@NoArgsConstructor
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