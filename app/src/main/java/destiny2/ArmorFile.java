package destiny2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Armor File parser
 */
public class ArmorFile {
    //-------------------------------------------------------------------------
    // Instance Variables

    // Line counter; used while parsing
    transient private int lineNumber = 0;

    private final List<Armor> pieces = new ArrayList<>();

    //-------------------------------------------------------------------------
    // Constructor

    /**
     * Parses the file
     * @param armorFile The armor file
     * @throws AppError On input error
     */
    public ArmorFile(File armorFile) throws AppError {
        try {
            Files.lines(armorFile.toPath())
                .map(line -> {
                    ++lineNumber;
                    return line.trim();
                })
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith("#"))
                .forEach(line -> addPiece(parseArmor(lineNumber, line)));
        } catch (IOException ex) {
            throw new AppError("I/O Error reading data: " + ex.getMessage());
        }
    }

    private Armor parseArmor(int lineNumber, String line) throws AppError {
        Scanner scanner = new Scanner(line).useDelimiter("\\s*,\\s*");

        try {
            var type = Type.valueOf(scanner.next());
            var rarity = Rarity.valueOf(scanner.next());
            var name = scanner.next().trim();
            var armor = new Armor(type, rarity, name);

            Stat.stream().forEach(stat -> armor.put(stat, scanner.nextInt()));
            return armor;
        } catch (Exception ex) {
            throw new AppError("Line " + lineNumber + ", " + ex.getMessage());
        }
    }

    private void addPiece(Armor piece) {
        if (!pieces.contains(piece)) {
            pieces.add(piece);
        }
    }

    //-------------------------------------------------------------------------
    // Public API

    public List<Armor> getPieces() {
        return pieces;
    }


}
