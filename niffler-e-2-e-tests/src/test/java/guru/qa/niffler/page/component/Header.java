package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($("header"));
    }

    public FriendsPage toFriendsPage() {
        self.$("button").click();
        $("a[href='/people/friends']").click();
        return new FriendsPage();
    }

    public AllPeoplePage toAllPeoplesPage() {
        self.$("button").click();
        $("a[href='/people/all']").click();
        return new AllPeoplePage();
    }

    public ProfilePage toProfilePage() {
        self.$("button").click();
        $("a[href='/profile']").click();
        return new ProfilePage();
    }

    public LoginPage signOut() {
        self.$("button").click();
        $("Sign out").click();
        return new LoginPage();
    }

    public EditSpendingPage addSpendingPage() {
        self.$("a.MuiButtonBase-root").click();
        return new EditSpendingPage();
    }

    public MainPage toMainPage() {
        self.$("a.link").click();
        return new MainPage();
    }

    public SelenideElement avatar() {
        return self.$("button img");
    }
}
