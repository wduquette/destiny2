package armory;

import armory.types.*;
import armory.util.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A tool to import pieces of armor from a Destiny Item Manager (DIM)
 * armor CSV file.
 */
public class ImportTool implements Tool {
    private static final String NAME = "Name";

    private static final String TIER = "Tier";
    private static final String LEGENDARY = "Legendary";
    private static final String EXOTIC = "Exotic";

    private static final String TYPE = "Type";
    private static final String HELMET = "Helmet";
    private static final String CHEST_ARMOR = "Chest Armor";
    private static final String GAUNTLETS = "Gauntlets";
    private static final String LEG_ARMOR = "Leg Armor";
    private static final String WARLOCK_BOND = "Warlock Bond";
    private static final String TITAN_MARK = "Titan Mark";
    private static final String HUNTER_CLOAK = "Hunter Cloak";

    private static final String EQUIPPABLE = "Equippable";

    private static final String MOBILITY_BASE = "Mobility (Base)";
    private static final String RESILIENCE_BASE = "Resilience (Base)";
    private static final String RECOVERY_BASE = "Recovery (Base)";
    private static final String DISCIPLINE_BASE = "Discipline (Base)";
    private static final String INTELLECT_BASE = "Intellect (Base)";
    private static final String STRENGTH_BASE = "Strength (Base)";

    private static final String EQUIPPED = "Equipped";
    private static final String TRUE = "true";

    private static final String OWNER = "Owner";
    private static final String VAULT = "Vault";

    private static final Set<String> TIERS_OF_INTEREST =
        Set.of(EXOTIC, LEGENDARY);

    private static final Set<String> BORING_TYPES =
        Set.of(WARLOCK_BOND, TITAN_MARK, HUNTER_CLOAK);

    //-------------------------------------------------------------------------
    // Instance Variables

    // Character class to filter on, or null for all.
    private CharacterClass characterClass;

    //-------------------------------------------------------------------------
    // Constructor

    public ImportTool() {
        // Nothing to do
    }

    //-------------------------------------------------------------------------
    // Application Code

    @Override public String usage() {
        return "armory import <armory.csv> [options...]";
    }

    @Override public String oneLiner() {
        return "Imports armor from a DIM armor CSV file.";
    }

    @Override public String help() {
        return """
Reads a Destiny Item Manager (DIM) armor CSV file and converts it
into an Armory file.  The options are as follows:

    -class         -- warlock, titan, or hunter
""";
    }

    /**
     * Invokes the tool given the arguments.
     *
     * @param args The command line arguments for this tool
     */
    public void start(Deque<String> args) {
        // FIRST, parse the command line
        if (args.isEmpty()) {
            System.out.println("Usage: " + usage());
            System.exit(1);
        }

        var csvFile = args.poll();


        // NEXT, load the CSV.
        CSVReader dim;

        try {
            dim = new CSVReader(new File(csvFile));
        } catch (IOException | CSVReader.CSVException ex) {
            throw new AppError(ex.getMessage());
        }

        // NEXT, parse the options.
        parseOptions(args);

        // NEXT, convert the rows into Armor values.
        var pieces = convertPieces(dim);

        // TODO: output file if need be.
        if (characterClass != null) {
            println("# Armory File: " + characterClass.toString().toLowerCase());
        } else {
            println("# Armory File: All Classes");
        }
        println("#");
        println("# * = Equipped, - = Carried");
        println();
        pieces.forEach(a -> println(a.asArmoryFileRow()));
    }

    private List<Armor> convertPieces(CSVReader dim) {
        var result = new ArrayList<Armor>();

        for (int i = 0; i < dim.getNumRows(); i++) {
            convertPiece(dim, i).ifPresent(result::add);
        }

        return result;
    }

    private Optional<Armor> convertPiece(CSVReader dim, int row) {
        // FIRST, is it for the class we care about?
        var equippable = dim.get(row, EQUIPPABLE).toUpperCase();

        if (characterClass != null &&
            !characterClass.toString().equals(equippable))
        {
            return Optional.empty();
        }

        // NEXT, is it a bond, mark, or cloak?
        var typeStr = dim.get(row, TYPE);

        if (BORING_TYPES.contains(typeStr)) {
            return Optional.empty();
        }

        // NEXT, is it at least legendary?
        var tierStr = dim.get(row, TIER);

        if (!TIERS_OF_INTEREST.contains(tierStr)) {
            return Optional.empty();
        }

        // NEXT, get the remaining data.
        try {
            var name = convertName(dim, row);
            var rarity = tierStr.equals(EXOTIC) ? Rarity.EXOTIC : Rarity.LEGEND;
            var type = switch (dim.get(row,TYPE)) {
                case HELMET -> Type.HEAD;
                case GAUNTLETS -> Type.ARMS;
                case CHEST_ARMOR -> Type.BODY;
                case LEG_ARMOR -> Type.LEGS;
                default -> throw new AppError("Unexpected armor type");
            };

            var piece = new Armor(type, rarity, name);

            piece.put(Stat.MOB, Integer.parseInt(dim.get(row, MOBILITY_BASE)));
            piece.put(Stat.RES, Integer.parseInt(dim.get(row, RESILIENCE_BASE)));
            piece.put(Stat.REC, Integer.parseInt(dim.get(row, RECOVERY_BASE)));
            piece.put(Stat.DIS, Integer.parseInt(dim.get(row, DISCIPLINE_BASE)));
            piece.put(Stat.INT, Integer.parseInt(dim.get(row, INTELLECT_BASE)));
            piece.put(Stat.STR, Integer.parseInt(dim.get(row, STRENGTH_BASE)));

            return Optional.of(piece);
        } catch (Exception ex) {
            throw new AppError("Could not import row at line " + (row + 2) +
                ", " + ex.getMessage());
        }
    }

    public String convertName(CSVReader dim, int row) {
        var name = dim.get(row, NAME);

        if (dim.get(row, EQUIPPED).equals(TRUE)) {
            return "*" + name;
        } else if (!dim.get(row, OWNER).equals(VAULT)) {
            // In inventory
            return "-" + name;
        } else {
            return name;
        }

    }

    /**
     * Parses the options and makes them available to the application.
     * @param opts The command line options
     * @throws AppError On input error
     */
    void parseOptions(Deque<String> opts) throws AppError {
        while (!opts.isEmpty()) {
            var opt = opts.poll();

            if (!opt.startsWith("-")) {
                throw new AppError("Expected an option: " + opt);
            }

            switch (opt) {
                case "-class" ->
                    characterClass = requireCharacterClass(opt, opts);
                default ->
                    throw new AppError("Unknown option: " + opt);
            }
        }
    }

    private String requireString(String opt, Deque<String> opts)
        throws AppError
    {
        if (opts.isEmpty()) {
            throw new AppError("Missing value for " + opt);
        }

        return opts.poll();
    }

    private CharacterClass requireCharacterClass(String opt, Deque<String> opts)
        throws AppError
    {
        var valueString = requireString(opt, opts);

        try {
            return CharacterClass.valueOf(valueString.toUpperCase());
        } catch (Exception ex) {
            throw new AppError("Invalid " + opt + " value: " + valueString);
        }
    }
}
