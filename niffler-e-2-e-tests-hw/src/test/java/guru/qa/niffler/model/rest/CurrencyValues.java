package guru.qa.niffler.model.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.HashMap;

@Getter
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public enum CurrencyValues {

    RUB("₽"), USD("$"), EUR("€"), KZT("₸");
    private final String symbol;

    @Nullable
    public static CurrencyValues getEnumBySymbol(String symbol){
        return Arrays.stream(values()).filter(s->s.symbol.equals(symbol)).findFirst().orElse(null);
    }

}