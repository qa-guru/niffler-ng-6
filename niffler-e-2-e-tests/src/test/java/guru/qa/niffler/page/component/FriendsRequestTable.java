package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class FriendsRequestTable {
    private final SelenideElement self = $("#requests");
    private final  SearchField searchField = new SearchField($("input[aria-label='search']"));

    public FriendsRequestTable acceptFriendRequest(String username){

        searchField.search(username);
        self.$$("td").find(text(username)).$$("td").get(1).$("Accept").click();
        return this;
    }

    public FriendsRequestTable declineFriendRequest(String username) {
        searchField.search(username);
        self.$$("td").find(text(username)).$$("td").get(1).$("Decline").click();
        return this;
    }
}
