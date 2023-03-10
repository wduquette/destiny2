package armory;

import armory.types.AppError;

import java.util.Deque;

/**
 * One of the application's subcommands.
 */
public interface Tool {
    void start(Deque<String> args) throws AppError;

    /** The tool's usage string. */
    String usage();

    /** A one-line description of the tool. */
    String oneLiner();

    /** The tool's full help text, not including the usage string. */
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
