package armory;

import armory.types.*;
import armory.util.LineReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Armor File parser
 */
public class ArmorFile {
    //-------------------------------------------------------------------------
    // Instance Variables

    // A reader for the lines in the file.
    transient private LineReader reader;

    // The complete suits of armor loaded from the file
    private final List<Suit> suits = new ArrayList<>();

    // The pieces of armor loaded from the file.
    private final List<Armor> pieces = new ArrayList<>();

    // The line number for each piece of armor.
    private final Map<Armor,Integer> piece2line = new HashMap<>();

    //-------------------------------------------------------------------------
    // Constructor

    /**
     * Parses the file
     * @param armorFile The armor file
     * @throws AppError On input error
     */
    public ArmorFile(File armorFile) throws AppError {
        try {
            reader = new LineReader(armorFile);

            parseFile();
        } catch (IOException ex) {
            throw new AppError("I/O Error reading data: " + ex.getMessage());
        } finally {
            reader = null;
        }
    }

    //-------------------------------------------------------------------------
    // The parser

    private void parseFile() throws AppError {
        while (!reader.isEmpty()) {
            var line = reader.next().trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            var scanner = new Scanner(line).useDelimiter("\\s+");

            if (scanner.hasNext("suit")) {
                addSuit(parseSuit(line));
                reader.next();  // For now, skip it.
            } else {
                addPiece(parsePiece(line));
            }
        }
    }

    private void addSuit(Suit suit) {
        suits.add(suit);
    }

    // Parses the suit name from the line. The pieces are parsed from the four
    // subsequent lines.
    private Suit parseSuit(String line) {
        var scanner = new Scanner(line).useDelimiter("\\s+");

        var suit = new Suit();
        scanner.next(); // Skip "suit"
        suit.setName(parseName(scanner));

        Type.forEach(type -> suit.put(type, parsePiece(type, reader.next())));

        return suit;
    }


    // Adds the piece of armor to the list, remembering its line number.
    private void addPiece(Armor piece) {
        if (!pieces.contains(piece)) {
            pieces.add(piece);
            piece2line.put(piece, reader.lineNumber());
        }
    }

    private Armor parsePiece(Type type, String line) {
        var piece = parsePiece(line);

        if (piece.type() != type) {
            throw new AppError("Expected " + type + " at line " +
                reader.lineNumber());
        }

        return piece;
    }

    // Parses the piece of armor from the given line
    private Armor parsePiece(String line) throws AppError {
        Scanner scanner = new Scanner(line).useDelimiter("\\s+");

        try {
            var type = Type.valueOf(scanner.next());
            var rarity = Rarity.valueOf(scanner.next());
            var name = parseName(scanner).trim();
            var armor = new Armor(type, rarity, name);

            Stat.stream().forEach(stat -> armor.put(stat, scanner.nextInt()));
            return armor;
        } catch (Exception ex) {
            throw new AppError("Line " + reader.lineNumber() + ", " + ex.getMessage());
        }
    }

    // A name is DOUBLE_QUOTE text DOUBLE_QUOTE
    private String parseName(Scanner scanner) {
        scanner.skip("\\s*\"");

        var name = scanner.findInLine("[^\"]+");
        scanner.skip("\"");
        return name;
    }

    //-------------------------------------------------------------------------
    // Public API

    /**
     * Gets the list of armor pieces read from the file.
     * @return The list
     */
    public List<Armor> getPieces() {
        return pieces;
    }

    public List<Suit> getSuits() {
        return suits;
    }

    /**
     * Gets the line number of the piece of armor in the file, or -1 if
     * the armor was not found.
     * @param piece The piece of armor
     * @return The line number, 1 to N, or -1 if not found.
     */
    public int getLineNumber(Armor piece) {
        return piece2line.getOrDefault(piece, -1);
    }
}
