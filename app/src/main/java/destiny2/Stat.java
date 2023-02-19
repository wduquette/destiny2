package destiny2;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

public enum Stat {
    MOB,
    RES,
    REC,
    DIS,
    INT,
    STR;

    public static Stream<Stat> stream() {
        return Arrays.stream(values());
    }

    public static void forEach(Consumer<Stat> consumer) {
        Arrays.stream(values()).forEach(consumer);
    }
}
