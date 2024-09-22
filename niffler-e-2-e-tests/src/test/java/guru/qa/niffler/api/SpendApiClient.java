package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.SpendJson;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SpendApiClient {
  private static final Config CFG = Config.getInstance();

  private final OkHttpClient okHttpClient = getOkHttpClient();

  public OkHttpClient getOkHttpClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(Level.BODY);
    return new OkHttpClient.Builder().addInterceptor(logging).build();
  }

  private final Retrofit retrofit = new Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @SneakyThrows
  public SpendJson createSpend(SpendJson spend) {
    return spendApi.createSpend(spend)
        .execute()
        .body();
  }
}