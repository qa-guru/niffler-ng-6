package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CurrenciesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CurrenciesGraphQlTest extends BaseGraphQlTest {

  @User
  @Test
  @ApiLogin
  void allCurrenciesShouldBeReturnedFromGateway(@Token String bearerToken) {
    final ApolloCall<CurrenciesQuery.Data> currenciesCall = apolloClient.query(new CurrenciesQuery())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<CurrenciesQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final CurrenciesQuery.Data data = response.dataOrThrow();
    final List<CurrenciesQuery.Currency> all = data.currencies;
    Assertions.assertEquals(
        CurrencyValues.RUB.name(),
        all.get(0).currency.rawValue
    );
    Assertions.assertEquals(
        CurrencyValues.KZT.name(),
        all.get(1).currency.rawValue
    );
    Assertions.assertEquals(
        CurrencyValues.EUR.name(),
        all.get(2).currency.rawValue
    );
    Assertions.assertEquals(
        CurrencyValues.USD.name(),
        all.get(3).currency.rawValue
    );
  }

}
