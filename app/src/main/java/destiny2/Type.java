package destiny2;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * The kind of armor.
 */
public enum Type {
    HEAD,
    ARMS,
    BODY,
    LEGS;

    public static Stream<Type> stream() {
        return Arrays.stream(values());
    }

}
