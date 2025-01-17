package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatGraphQlTest extends BaseGraphQlTest {

  @User
  @Test
  @ApiLogin
  void statTest(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(null)
            .statCurrency(null)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;
    Assertions.assertEquals(
        0.0,
        result.total
    );
  }

}
