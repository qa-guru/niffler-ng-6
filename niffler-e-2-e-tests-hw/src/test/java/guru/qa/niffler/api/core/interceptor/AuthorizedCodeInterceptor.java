package guru.qa.niffler.api.core.interceptor;

import guru.qa.niffler.api.core.store.AuthStore;
import okhttp3.Interceptor;
import okhttp3.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

public class AuthorizedCodeInterceptor implements Interceptor {

    @Override
    public @Nonnull Response intercept(@Nonnull Chain chain) throws IOException {
        String code = chain.request().url().queryParameter("code");
        if (isNotEmpty(code))
            AuthStore.INSTANCE.setCurrentUserCode(code);
        return chain.proceed(chain.request());
    }

}
