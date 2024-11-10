package guru.qa.niffler.api.core;

import guru.qa.niffler.config.Config;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

@ParametersAreNonnullByDefault
public abstract class RestClient {

//    protected static final Config CFG = Config.getInstance();
//    private static final boolean DEFAULT_FOLLOW_REDIRECT = false;
//    private static final Converter.Factory DEFAULT_CONVERTER = JacksonConverterFactory.create();
//    private static final Level DEFAULT_LOG_LEVEL = HEADERS;
//    private static final Interceptor[] DEFAULT_INTERCEPTORS = new Interceptor[0];
//
//    private final OkHttpClient okHttpClient;
//    protected final Retrofit retrofit;
//
//    public RestClient(String baseUrl) {
//        this(baseUrl, DEFAULT_FOLLOW_REDIRECT, DEFAULT_CONVERTER, DEFAULT_LOG_LEVEL, DEFAULT_INTERCEPTORS);
//    }
//
//    public RestClient(String baseUrl, boolean followRedirect) {
//        this(baseUrl, followRedirect, DEFAULT_CONVERTER, DEFAULT_LOG_LEVEL, DEFAULT_INTERCEPTORS);
//    }
//
//    public RestClient(String baseUrl, HttpLoggingInterceptor.Level loggingLevel) {
//        this(baseUrl, DEFAULT_FOLLOW_REDIRECT, DEFAULT_CONVERTER, loggingLevel, DEFAULT_INTERCEPTORS);
//    }
//
//    public RestClient(String baseUrl, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
//        this(baseUrl, DEFAULT_FOLLOW_REDIRECT, converterFactory, loggingLevel, DEFAULT_INTERCEPTORS);
//    }
//
//    public RestClient(String baseUrl, boolean followRedirect, HttpLoggingInterceptor.Level loggingLevel) {
//        this(baseUrl, followRedirect, DEFAULT_CONVERTER, loggingLevel, DEFAULT_INTERCEPTORS);
//    }
//
//    public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
//        this(baseUrl, followRedirect, converterFactory, loggingLevel, DEFAULT_INTERCEPTORS);
//    }
//
//    public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel, Interceptor... interceptors) {
//
//        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
//                .followRedirects(followRedirect);
//        Arrays.stream(interceptors)
//                .forEach(okHttpBuilder::addInterceptor);
//        okHttpBuilder.addNetworkInterceptor(
//                new HttpLoggingInterceptor().setLevel(loggingLevel));
//        okHttpBuilder.cookieJar(
//                new JavaNetCookieJar(
//                        new CookieManager(
//                                ThreadSafeCookieStore.INSTANCE,
//                                CookiePolicy.ACCEPT_ALL
//                        )
//                )
//        );
//
//        this.okHttpClient = okHttpBuilder.build();
//
//        this.retrofit = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(baseUrl)
//                .addConverterFactory(converterFactory)
//                .build();
//    }

    protected static final Config CFG = Config.getInstance();

    private final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    public RestClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), HEADERS, new Interceptor[0]);
    }

    public RestClient(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), HEADERS, new Interceptor[0]);
    }

    public RestClient(String baseUrl, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, JacksonConverterFactory.create(), loggingLevel, new Interceptor[0]);
    }

    public RestClient(String baseUrl, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, converterFactory, loggingLevel, new Interceptor[0]);
    }

    public RestClient(String baseUrl, boolean followRedirect, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), loggingLevel, new Interceptor[0]);
    }

    public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, followRedirect, converterFactory, loggingLevel, new Interceptor[0]);
    }

    public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel, @Nonnull Interceptor... interceptors) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .followRedirects(followRedirect);

        for (Interceptor interceptor : interceptors) {
            okHttpBuilder.addNetworkInterceptor(interceptor);
        }

        okHttpBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel));
        okHttpBuilder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(
                                ThreadSafeCookieStore.INSTANCE,
                                CookiePolicy.ACCEPT_ALL
                        )
                )
        );

        this.okHttpClient = okHttpBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build();
    }

    public <T> T create(final Class<T> service) {
        return this.retrofit.create(service);
    }


}
