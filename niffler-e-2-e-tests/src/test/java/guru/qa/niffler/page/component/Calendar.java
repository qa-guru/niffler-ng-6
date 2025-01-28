package guru.qa.niffler.page.component;
import com.codeborne.selenide.SelenideElement;

import java.util.Date;

public class Calendar extends BaseComponent<Calendar> {

    public Calendar(SelenideElement self) {
        super(self);
    }

    public Calendar selectDateInCalendar(Date date) {
        self.setValue(date.toString());
        return this;
    }
}
