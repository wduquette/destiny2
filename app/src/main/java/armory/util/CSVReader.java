package armory.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class reads CSV files with the following characteristics:
 *
 * <ul>
 *     <li>The first line is a row of column names.</li>
 *     <li>Column values may be empty.</li>
 *     <li>String values may contain spaces but not commas.</li>
 * </ul>
 */
public class CSVReader {

    //-------------------------------------------------------------------------
    // Instance Variables

    // The list of column names
    private final List<String> columns = new ArrayList<>();

    // The list of rows
    private final List<List<String>> rows = new ArrayList<>();

    //-------------------------------------------------------------------------
    // Constructor

    /**
     * Reads the CSV file.
     * @param csvFile The file
     * @throws IOException If the file could not be read from disk
     * @throws CSVException If the file could not be parsed.
     */
    public CSVReader(File csvFile) throws IOException, CSVException {
        // FIRST, open the file
        var reader = new LineReader(csvFile);

        if (reader.isEmpty()) {
            throw new CSVException(1, "Input file is empty.");
        }

        // NEXT, get the column names.
        columns.addAll(parseColumnNames(reader.next()));

        // NEXT, get the rows of data.
        while (!reader.isEmpty()) {
            rows.add(parseRow(reader.lineNumber(), reader.next()));
        }
    }

    /**
     * Parses the column name line from the file.
     * @param line The text of the line
     * @return A list of the column names.
     */
    private List<String> parseColumnNames(String line) {
        var scanner = new Scanner(line).useDelimiter(",");
        var result = new ArrayList<String>();
        System.out.println("Columns: ");

        while (scanner.hasNext()) {
            result.add(scanner.next());

        }

        return result;
    }

    /**
     * Parses a data line.
     * @param lineNumber The line number
     * @param line The line's text
     * @return A list of values
     * @throws CSVException if the number of values doesn't match the expected
     * number of columns.
     */
    private List<String> parseRow(int lineNumber, String line)
        throws CSVException
    {
        var scanner = new Scanner(line).useDelimiter(",");
        var result = new ArrayList<String>();

        while (scanner.hasNext()) {
            result.add(scanner.next());
        }

        // hasNext() is false at the last column, if the value is empty.
        if (result.size() == columns.size() - 1) {
            result.add("");
        }

        if (result.size() != columns.size()) {
            throw new CSVException(lineNumber,
                "Expected " + columns.size() +
                " columns, found: " + result.size());
        }

        return result;
    }

    //-------------------------------------------------------------------------
    // Public Methods

    /**
     * Gets the number of data columns in the file
     * @return The number
     */
    public int getNumColumns() {
        return columns.size();
    }

    /**
     * Gets the number of data rows in the file
     * @return The number
     */
    public int getNumRows() {
        return rows.size();
    }

    /**
     * Gets the value of the given row and column.
     * @param row The row number
     * @param column The column number
     * @return The value
     */
    public String get(int row, int column) {
        return rows.get(row).get(column);
    }

    /**
     * Gets the value of the given row and column.
     * @param row The row number
     * @param column The column name
     * @return The value
     */
    public String get(int row, String column) {
        var ndx = columns.indexOf(column);

        if (ndx == -1) {
            throw new IllegalArgumentException(
                "Invalid column name: \"" + column + "\"");
        }

        return get(row, ndx);
    }


    //-------------------------------------------------------------------------
    // Helpers

    /**
     * The class for records in the file.
     */
    public static class Record extends HashMap<String,String> { }

    public static class CSVException extends Exception {
        private final int lineNumber;

        public CSVException(int lineNumber, String message) {
            super("Line " + lineNumber + ", " + message);
            this.lineNumber = lineNumber;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }
}
