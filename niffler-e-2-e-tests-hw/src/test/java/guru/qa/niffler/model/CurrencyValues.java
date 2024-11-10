package guru.qa.niffler.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public enum CurrencyValues {
    RUB("₽"), USD("$"), EUR("€"), KZT("₸");
    private final String symbol;
}