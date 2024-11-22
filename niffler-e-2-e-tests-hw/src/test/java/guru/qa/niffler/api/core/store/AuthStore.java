package guru.qa.niffler.api.core.store;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public enum AuthStore {

    INSTANCE;

    // Map<username, code>
    private static final ThreadLocal<Map<String, String>> threadSafeAuthCodeStore = ThreadLocal.withInitial(HashMap::new);

    // Users authorization token.
    private static final ThreadLocal<Map<String, String>> threadSafeAuthTokenStore = ThreadLocal.withInitial(HashMap::new);

    private static final ThreadLocal<String> currentUser = ThreadLocal.withInitial(String::new);

    @Nullable
    public String getCode(String username) {
        return threadSafeAuthCodeStore.get().get(username);
    }

    public String getCurrentUserCode() {
        return threadSafeAuthCodeStore.get().get(currentUser.get());
    }

    public void setCode(String username, String code) {
        threadSafeAuthCodeStore.get().put(username, code);
    }

    public void setCurrentUserCode(String token) {
        threadSafeAuthCodeStore.get().put(currentUser.get(), token);
    }

    @Nullable
    public String getToken(String username) {
        return threadSafeAuthTokenStore.get().get(username);
    }

    public String getCurrentUserToken() {
        return threadSafeAuthTokenStore.get().get(currentUser.get());
    }

    public void setToken(String username, String token) {
        threadSafeAuthTokenStore.get().put(username, token);
    }

    public void setCurrentUserToken(String token) {
        threadSafeAuthTokenStore.get().put(currentUser.get(), token);
        return;
    }

    public String getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(String username) {
        currentUser.set(username);
    }

}
