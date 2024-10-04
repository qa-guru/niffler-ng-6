package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SpendJson{

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("spendDate")
    private Date spendDate;

    @JsonProperty("category")
    private CategoryJson category;

    @JsonProperty("currency")
    private CurrencyValues currency;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

}