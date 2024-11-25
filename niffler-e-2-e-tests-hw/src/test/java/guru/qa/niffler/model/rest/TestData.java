package guru.qa.niffler.model.rest;

import guru.qa.niffler.ex.CookieNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TestData {

    @Builder.Default
    private List<CategoryJson> categories = new ArrayList<>();

    @Builder.Default
    private List<SpendJson> spendings = new ArrayList<>();

    @Builder.Default
    private List<UserJson> incomeInvitations = new ArrayList<>();

    @Builder.Default
    private List<UserJson> outcomeInvitations = new ArrayList<>();

    @Builder.Default
    private List<UserJson> friends = new ArrayList<>();

    @Builder.Default
    private List<HttpCookie> cookies = new ArrayList<>();

    private String token;

    public @Nullable String getCookieValueByName(@Nonnull String cookieName) {
        return cookies.stream()
                .filter(cookie -> cookie.getName().equalsIgnoreCase(cookieName))
                .findFirst()
                .orElseThrow(() -> new CookieNotFoundException("Cookie not found with name = [%s]"))
                .getValue();
    }
}
