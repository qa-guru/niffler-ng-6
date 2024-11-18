package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SpendJson {

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

    public String getAmountWithSymbol() {
        var amountVal = this.amount % 10 == 0
                ? this.amount.intValue()
                : this.amount;
        return amountVal + " " + this.currency.getSymbol();
    }

    public String getDateVal() {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(this.spendDate);
    }

}