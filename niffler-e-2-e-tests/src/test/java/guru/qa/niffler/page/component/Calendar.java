package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import java.util.Date;

public class Calendar {
    private final SelenideElement self;

    public Calendar(SelenideElement self) {
        this.self = self;
    }

    public Calendar selectDateInCalendar(Date date) {
        self.setValue(date.toString());
        return this;
    }
}
