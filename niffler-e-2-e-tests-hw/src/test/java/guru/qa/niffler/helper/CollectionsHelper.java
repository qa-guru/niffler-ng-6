package guru.qa.niffler.helper;

import java.util.Collection;

public class CollectionsHelper {

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    public static <T extends Number & Comparable<? super Number>> boolean isContainsNullOrZero(Collection<T> collection) {
        return collection.stream().anyMatch(x -> x == null || x.compareTo(0) == 0);
    }

}
