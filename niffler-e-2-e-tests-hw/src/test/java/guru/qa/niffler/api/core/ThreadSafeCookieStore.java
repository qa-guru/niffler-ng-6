package guru.qa.niffler.api.core;

import guru.qa.niffler.api.core.store.AuthStore;
import guru.qa.niffler.enums.CookieType;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ThreadSafeCookieStore implements CookieStore {

    INSTANCE;

    private final ThreadLocal<Map<String, CookieStore>> threadSafeCookieStore = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void add(URI uri, HttpCookie cookie) {
        getStore().add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getStore().get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return getStore().getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return getStore().getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return getStore().remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return getStore().removeAll();
    }

    public void removeAllUsersStores() {
        threadSafeCookieStore.remove();
    }

    private CookieStore getStore() {
        return threadSafeCookieStore.get()
                .computeIfAbsent(
                        AuthStore.INSTANCE.getCurrentUser(),
                        user -> new CookieManager().getCookieStore());
    }

    public String cookieValue(String cookieName) {
        return getCookies().stream()
                .filter(c -> c.getName().equals(cookieName))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElseThrow();
    }

    public List<HttpCookie> getUserCookies(String username) {
        return threadSafeCookieStore.get().get(username).getCookies();
    }

    public String cookieValue(@Nonnull String username, @Nonnull String cookieName) {
        return getUserCookies(username).stream()
                .filter(c -> c.getName().equals(cookieName))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElseThrow();
    }

    public String cookieValue(@Nonnull String username, @Nonnull CookieType cookieType) {
        return getUserCookies(username).stream()
                .filter(c -> c.getName().equals(cookieType.getCookieName()))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElseThrow();
    }

    @Nonnull
    public Map<String, String> cookiesValue(
            @Nonnull String cookieName,
            @Nullable String... cookieNames) {

        List<String> cookieNamesList = Arrays.stream(
                ArrayUtils.addFirst(
                        cookieNames,
                        cookieName)
        ).toList();

        return getCookies().stream()
                .filter(c -> cookieNamesList.contains(c.getName()))
                .collect(
                        Collectors.toMap(
                                HttpCookie::getName,
                                HttpCookie::getValue));

    }

    @Nonnull
    public Map<String, String> cookiesValue(
            @Nonnull String username,
            @Nonnull String cookieName,
            @Nullable String... cookieNames) {

        List<String> cookieNamesList = Arrays.stream(
                ArrayUtils.addFirst(
                        cookieNames,
                        cookieName)
        ).toList();

        return getUserCookies(username).stream()
                .filter(c -> cookieNamesList.contains(c.getName()))
                .collect(
                        Collectors.toMap(
                                HttpCookie::getName,
                                HttpCookie::getValue));

    }

}
