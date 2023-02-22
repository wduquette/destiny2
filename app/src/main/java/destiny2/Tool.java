package destiny2;

import java.util.Deque;

/**
 * One of the application's subcommands.
 */
public interface Tool {
    void start(Deque<String> args) throws AppError;

    String usage();
    String help();

    default void println(String text) {
        System.out.println(text);
    }

    default void println() {
        System.out.println();
    }

    default void printf(String fmt, Object... args) {
        System.out.printf(fmt, args);
    }
}
