package guru.qa.niffler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyValues {
    RUB("₽"), USD("$"), EUR("€"), KZT("₸");
    private final String symbol;
}