package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SumByCategory {

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("currency")
    private CurrencyValues currency;

    @JsonProperty("sum")
    private double sum;

    @JsonProperty("firstSpendDate")
    private Date firstSpendDate;

    @JsonProperty("lastSpendDate")
    private Date lastSpendDate;

}
