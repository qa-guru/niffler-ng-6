package guru.qa.niffler.service;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;

import java.net.CookieManager;
import java.net.CookiePolicy;

import static  okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

public abstract class RestClient {

    private final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;

    public RestClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), BODY, null);
    }

    public RestClient(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), BODY, null);
    }

    public RestClient(String baseUrl, boolean followRedirect, @Nullable Interceptor... interceptors) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), HEADERS, interceptors);
    }

    public RestClient(String baseUrl, boolean followRedirect, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, followRedirect, JacksonConverterFactory.create(), loggingLevel, null);
    }

    public RestClient(String baseUrl, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, JacksonConverterFactory.create(), loggingLevel, null);
    }

    public RestClient(String baseUrl, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl,false, converterFactory, loggingLevel, null);
    }

    public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl,followRedirect, converterFactory, loggingLevel, null);
    }

    public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel, @Nullable Interceptor... interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .followRedirects(followRedirect);
        if(interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpClientBuilder.addNetworkInterceptor(interceptor);
            }
        }
        okHttpClientBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel));
        okHttpClientBuilder.addNetworkInterceptor(new AllureOkHttp3().setRequestTemplate("html-request-attachment.ftl").setResponseTemplate("html-response-attachment.ftl"));
        okHttpClientBuilder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(
                                ThreadSafeCookieStore.INSTANCE,
                                CookiePolicy.ACCEPT_ALL
                        )
                )
        );

        this.okHttpClient = okHttpClientBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .build();
    }

}