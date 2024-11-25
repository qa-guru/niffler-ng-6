package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StatisticJson {

    @JsonProperty("dateFrom")
    private Date dateFrom;

    @JsonProperty("dateTo")
    private Date dateTo;

    @JsonProperty("currency")
    private CurrencyValues currency;

    @JsonProperty("total")
    private Double total;

    @JsonProperty("userDefaultCurrency")
    private CurrencyValues userDefaultCurrency;

    @JsonProperty("totalInUserDefaultCurrency")
    private Double totalInUserDefaultCurrency;

    @JsonProperty("categoryStatistics")
    private List<StatisticByCategoryJson> categoryStatistics;

}
