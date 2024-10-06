package guru.qa.niffler.mapper;

import guru.qa.niffler.ex.InvalidDateException;
import guru.qa.niffler.helper.DateHelper;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;

public class SpendMapper {

    public SpendJson updateFromAnno(SpendJson spend, Spending anno) {
        var datePattern = "MM/dd/yyyy";
        var username = anno.username().isEmpty()
                ? spend.getUsername()
                : anno.username();

        // @formatter:off
        return SpendJson.builder()
                .id(spend.getId())
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
                                .username(username)
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
                .username(username)
                .build();
        // @formatter:on

    }

}