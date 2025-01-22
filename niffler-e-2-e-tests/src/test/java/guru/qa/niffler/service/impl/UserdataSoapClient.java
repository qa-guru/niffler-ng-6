package guru.qa.niffler.service.impl;

import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class UserdataSoapClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final UserdataSoapApi userdataSoapApi;

  public UserdataSoapClient() {
    super(CFG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
    this.userdataSoapApi = create(UserdataSoapApi.class);
  }

  @NotNull
  @Step("Get current user info using SOAP")
  public UserResponse currentUser(CurrentUserRequest request) throws IOException {
    return userdataSoapApi.currentUser(request).execute().body();
  }
}
