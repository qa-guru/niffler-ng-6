package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.ex.InvalidDateException;
import guru.qa.niffler.helper.DateHelper;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;

import java.util.Date;

public class SpendMapper {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    public SpendEntity toEntity(@NonNull SpendJson dto){
        return SpendEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .spendDate(new java.sql.Date(dto.getSpendDate().getTime()))
                .description(dto.getDescription())
                .category(categoryMapper.toEntity(dto.getCategory()))
                .build();
    }

    public SpendJson toDto(@NonNull SpendEntity entity){
        return SpendJson.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .spendDate(new Date(entity.getSpendDate().getTime()))
                .description(entity.getDescription())
                .category(categoryMapper.toDto(entity.getCategory()))
                .build();
    }

    public SpendJson updateFromAnno(@NonNull SpendJson spend, Spending anno) {
        var datePattern = "MM/dd/yyyy";

        // @formatter:off
        return SpendJson.builder()
                .id(spend.getId())
                .username(spend.getUsername())
                .spendDate(
                        anno.date().isEmpty()
                                ? spend.getSpendDate()
                                : DateHelper.parseDateByPattern(anno.date(), datePattern)
                                .orElseThrow(() -> new InvalidDateException("Can not parse date from text [%s] by pattern [%s]"
                                        .formatted(anno.date(), datePattern))))
                .category(
                        anno.category() == null
                                ? spend.getCategory()
                                : CategoryJson.builder()
                                .name(
                                        anno.category().isEmpty()
                                                ? spend.getCategory().getName()
                                                : anno.category())
                                .username(spend.getUsername())
                                .archived(false)
                                .build())
                .currency(
                        (anno.currency() == CurrencyValues.USD && !anno.notGenerateCurrency())
                                ? spend.getCurrency()
                                : anno.currency())
                .amount(
                        (anno.amount() == 0.0 && !anno.notGenerateAmount())
                                ? spend.getAmount()
                                : anno.amount())
                .description(
                        anno.description().isEmpty()
                                ? spend.getDescription()
                                : anno.description())
                .build();
        // @formatter:on

    }

}