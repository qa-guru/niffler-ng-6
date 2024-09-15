package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {
    private final SelenideElement friendItem = $x("//p[text()='dima']");
    private final SelenideElement emptyFriendList = $x("//p[text()='There are no users yet']");
    private final SelenideElement friendRequestList = $x("//h2[text()='Friend requests']");
    private final SelenideElement waitingMessage = $x("//span[text()='Waiting...']");
    private final ElementsCollection friendList = $$(".css-8va9ha");


    public void shouldFriendItem(String value) {
        friendItem.shouldHave(text(value));
    }

    public void shouldEmptyFriendList(String value) {
        emptyFriendList.shouldHave(text(value));
    }

    public void shouldFriendRequestList() {
        friendRequestList.shouldBe(visible);
    }

    public void shouldWaitingMessage() {
        waitingMessage.shouldBe(visible);
    }

    public void shouldFriendName(String friendName) {
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).text().equals(friendName)) {
                friendList.get(i).shouldHave(text(friendName));
                break;
            }
        }
    }


}
