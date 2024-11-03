package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class PeopleTable extends BaseComponent<PeopleTable> {

    public PeopleTable(SelenideElement self) {
        super($("table[aria-labelledby='tableTitle']"));
    }
}
