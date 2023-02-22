package armory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Armor File parser
 */
@SuppressWarnings("unused")
public class LineReader {
    //-------------------------------------------------------------------------
    // Instance Variables

    // Stream of lines to parse
    transient private final Queue<String> lines;

    // Line counter; used while parsing
    transient private int lineNumber = 0;

    //-------------------------------------------------------------------------
    // Constructor

    /**
     * Breaks the file into lines, to be read one at a time.
     * @param file The file
     */
    public LineReader(File file) throws IOException {
        lines = new ArrayDeque<>(Files.lines(file.toPath()).toList());
    }

    //-------------------------------------------------------------------------
    // Public Methods

    /**
     * Gets whether the reader has no more lines
     * @return true or false
     */
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    /**
     * Peeks at the next line.
     * @return The string
     */
    public String peek() {
        return lines.peek();
    }

    /**
     * Gets the next line, advancing past it.
     * @return The string
     */
    public String next() {
        ++lineNumber;
        return lines.poll();
    }

    /**
     * Gets the line number of the most recently read line (1 to N)
     * @return the number
     */
    public int lineNumber() {
        return lineNumber;
    }
}
