package armory.types;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Armor rarity.  There are other rarity levels, but these are the only
 * ones that matter.
 */
public enum Rarity {
    EXOTIC,
    LEGEND;

    public static Stream<Rarity> stream() {
        return Arrays.stream(values());
    }

    public static void forEach(Consumer<Rarity> consumer) {
        Arrays.stream(values()).forEach(consumer);
    }
}
