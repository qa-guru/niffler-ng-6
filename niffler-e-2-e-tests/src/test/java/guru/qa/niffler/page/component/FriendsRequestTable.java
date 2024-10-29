package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class FriendsRequestTable {
    private final SelenideElement self = $("#requests");
    private final  SearchField searchField = new SearchField($("input[aria-label='search']"));

    @Step()
    public FriendsRequestTable acceptFriendRequest(String username){
        searchField.search(username);
        self.$$("tr").find(text(username)).$(By.xpath("*//button[text()='Accept']")).click();
        return this;
    }

    public FriendsRequestTable declineFriendRequest(String username) {
        searchField.search(username);
        self.$$("tr").find(text(username)).$(By.xpath("*//button[text()='Decline']")).click();
        return this;
    }
}
