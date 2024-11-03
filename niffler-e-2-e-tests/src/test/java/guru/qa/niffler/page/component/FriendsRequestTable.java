package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsRequestTable extends BaseComponent<FriendsRequestTable> {

    private final  SearchField searchField = new SearchField($("input[aria-label='search']"));

    public FriendsRequestTable() {
        super($("#requests"));
    }

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

    @Step("Проверяем, что есть входящее предложение дружбы")
    public FriendsRequestTable checkIncomeInvitationFriend() {
        self.$$("tr").find(text("Accept")).should(visible);
        return this;
    }
}
