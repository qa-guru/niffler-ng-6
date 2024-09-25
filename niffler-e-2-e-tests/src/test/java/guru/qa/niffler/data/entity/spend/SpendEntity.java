package guru.qa.niffler.data.entity.spend;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
public class SpendEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private Date spendDate;
    private Double amount;
    private String description;
    private CategoryEntity category;

    public static SpendEntity fromJson(SpendJson json) {
        SpendEntity se = new SpendEntity();
        se.setId(json.id());
        se.setUsername(json.username());
        se.setCurrency(json.currency());
        se.setSpendDate(new java.sql.Date(json.spendDate().getTime()));
        se.setAmount(json.amount());
        se.setDescription(json.description());
        se.setCategory(
                CategoryEntity.fromJson(
                        json.category()
                )
        );
        return se;
    }
}