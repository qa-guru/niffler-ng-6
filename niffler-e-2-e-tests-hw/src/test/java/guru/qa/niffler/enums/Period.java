package guru.qa.niffler.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Period {
    ALL_TIME("All time"), LAST_MONTH("Last month"), LAST_WEEK("Last week"), TODAY("Today");
    private final String value;
}
