package guru.qa.niffler.mapper;

import guru.qa.niffler.ex.InvalidDateException;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.helper.DateHelper;

public class SpendMapper {

    public SpendJson updateFromAnno(SpendJson spend, Spending anno) {
        var datePattern = "MM/dd/yyyy";
        return new SpendJson(
                spend.id(),
                anno.date().isEmpty()
                        ? spend.spendDate()
                        : DateHelper.parseDateByPattern(anno.date(), datePattern)
                        .orElseThrow(() -> new InvalidDateException("Can not parse date from text [%s] by pattern [%s]"
                                .formatted(anno.date(), datePattern))),
                new CategoryJson(
                        null,
                        anno.category().isEmpty()
                                ? spend.category().name()
                                : anno.category(),
                        anno.username().isEmpty()
                                ? spend.username()
                                : anno.username(),
                        false),
                (anno.currency() == CurrencyValues.USD && !anno.notGenerateCurrency())
                        ? spend.currency()
                        : anno.currency(),
                (anno.amount() == 0.0 && !anno.notGenerateAmount())
                        ? spend.amount()
                        : anno.amount(),
                anno.description().isEmpty()
                        ? spend.description()
                        : anno.description(),
                anno.username().isEmpty()
                        ? spend.username()
                        : anno.username()
        );
    }

}
