package armory.types;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The type of armor.
 */
public enum Type {
    HEAD,
    ARMS,
    BODY,
    LEGS;

    public static Stream<Type> stream() {
        return Arrays.stream(values());
    }

    public static void forEach(Consumer<Type> consumer) {
        Arrays.stream(values()).forEach(consumer);
    }
}
