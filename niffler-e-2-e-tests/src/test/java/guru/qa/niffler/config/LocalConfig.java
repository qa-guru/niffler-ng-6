package guru.qa.niffler.config;


import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

enum LocalConfig implements Config {
  INSTANCE;

  @Nonnull
  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3000/";
  }

  @Nonnull
  @Override
  public String authUrl() {
    return "http://127.0.0.1:9000/";
  }

  @Nonnull
  @Override
  public String authJdbcUrl() {
    return "jdbc:postgresql://127.0.0.1:5432/niffler-auth";
  }

  @Nonnull
  @Override
  public String gatewayUrl() {
    return "http://127.0.0.1:8090/";
  }

  @Nonnull
  @Override
  public String userdataUrl() {
    return "http://127.0.0.1:8089/";
  }

  @Nonnull
  @Override
  public String userdataJdbcUrl() {
    return "jdbc:postgresql://127.0.0.1:5432/niffler-userdata";
  }

  @Nonnull
  @Override
  public String spendUrl() {
    return "http://127.0.0.1:8093/";
  }

  @Nonnull
  @Override
  public String spendJdbcUrl() {
    return "jdbc:postgresql://127.0.0.1:5432/niffler-spend";
  }

  @Nonnull
  @Override
  public String currencyJdbcUrl() {
    return "jdbc:postgresql://127.0.0.1:5432/niffler-currency";
  }

  @NotNull
  @Override
  public String currencyGrpcAddress() {
    return "127.0.0.1";
  }

  @NotNull
  @Override
  public String kafkaAddress() {
    return "127.0.0.1:9092";
  }
}
