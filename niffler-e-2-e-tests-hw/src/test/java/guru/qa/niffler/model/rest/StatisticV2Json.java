package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StatisticV2Json {

    @JsonProperty("total")
    private Double total;

    @JsonProperty("currency")
    private CurrencyValues currency;

    @JsonProperty("statByCategories")
    private List<SumByCategory> statByCategories;

}
