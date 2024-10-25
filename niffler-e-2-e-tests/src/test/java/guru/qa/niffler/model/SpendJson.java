package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.enums.CurrencyValuesEnum;

import java.util.Date;
import java.util.UUID;

public record SpendJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("spendDate")
    Date spendDate,
    @JsonProperty("category")
    CategoryJson category,
    @JsonProperty("currency")
    CurrencyValuesEnum currency,
    @JsonProperty("amount")
    Double amount,
    @JsonProperty("description")
    String description,
    @JsonProperty("username")
    String username) {

  public static SpendJson fromEntity(SpendEntity entity) {
    final CategoryEntity category = entity.getCategory();
    final String username = entity.getUsername();

    return new SpendJson(
        entity.getId(),
        entity.getSpendDate(),
        new CategoryJson(
            category.getId(),
            category.getName(),
            username,
            category.isArchived()
        ),
        entity.getCurrency(),
        entity.getAmount(),
        entity.getDescription(),
        username
    );
  }
}