package guru.qa.niffler.api.core;

import guru.qa.niffler.config.Config;
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

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

public abstract class RestClient {

  protected static final Config CFG = Config.getInstance();

  private final OkHttpClient okHttpClient;
  private final Retrofit retrofit;

  public RestClient(String baseUrl) {
    this(baseUrl, false, JacksonConverterFactory.create(), HEADERS, new Interceptor[0]);
  }

  public RestClient(String baseUrl, boolean followRedirect) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), HEADERS, new Interceptor[0]);
  }

  public RestClient(String baseUrl, boolean followRedirect, @Nullable Interceptor... interceptors) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), HEADERS, interceptors);
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

  public RestClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel, @Nullable Interceptor... interceptors) {
    OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
        .followRedirects(followRedirect);

    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        okHttpBuilder.addNetworkInterceptor(interceptor);
      }
    }
    okHttpBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel));
    okHttpBuilder.addNetworkInterceptor(new AllureOkHttp3());
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

  public static final class EmptyClient extends RestClient {
    public EmptyClient(String baseUrl) {
      super(baseUrl);
    }

    public EmptyClient(String baseUrl, boolean followRedirect) {
      super(baseUrl, followRedirect);
    }

    public EmptyClient(String baseUrl, HttpLoggingInterceptor.Level loggingLevel) {
      super(baseUrl, loggingLevel);
    }

    public EmptyClient(String baseUrl, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
      super(baseUrl, converterFactory, loggingLevel);
    }

    public EmptyClient(String baseUrl, boolean followRedirect, HttpLoggingInterceptor.Level loggingLevel) {
      super(baseUrl, followRedirect, loggingLevel);
    }

    public EmptyClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel) {
      super(baseUrl, followRedirect, converterFactory, loggingLevel);
    }

    public EmptyClient(String baseUrl, boolean followRedirect, Converter.Factory converterFactory, HttpLoggingInterceptor.Level loggingLevel, @org.jetbrains.annotations.Nullable Interceptor... interceptors) {
      super(baseUrl, followRedirect, converterFactory, loggingLevel, interceptors);
    }
  }
}
