/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package destiny2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ArmorApp {
    //-------------------------------------------------------------------------
    // Instance variables

    // The loaded pieces of armor
    private final Map<Type, List<Armor>> vault = new HashMap<>();

    //-------------------------------------------------------------------------
    // The App

    public void app(String[] args) {
        if (args.length == 0) {
            println("""
            Usage: armor armor.dat [options...]
                
                -limit num     -- Maximum number of results to display, default is 5
                -mob weight    -- default is 1.0 for each
                -res weight
                -rec weight
                -dis weight
                -int weight
                -str weight
                -minmob value  -- Minimum stat, default is 0
                -minres value  -- Minimum stat, default is 0
                -minrec value  -- Minimum stat, default is 0
                -mindis value  -- Minimum stat, default is 0
                -minint value  -- Minimum stat, default is 0
                -minstr value  -- Minimum stat, default is 0
            """);
            System.exit(1);
        }

        // NEXT, where are we?
        println("In: " + System.getProperty("user.dir"));

        // NEXT, parse the arguments
        var options = new Options(args);

        // FIRST, load the armor from the file.
        readVault(options.getFileName()).forEach(armor -> {
            vault.putIfAbsent(armor.type(), new ArrayList<>());
            vault.get(armor.type()).add(armor);
        });

        println("\nArmor from " + options.getFileName() + ":\n");
        dumpVault();

        // NEXT, get the current set.
        var current = makeSet(0, 0, 0, 0);

        println("\nEquipped set:\n");
        current.dump();

        // NEXT, generate the possible choices
        var sets = generateSets();

        var comparator = new ArmorComparator(options.getWeights());
        var mins = options.getMins();

        sets.sort(comparator.reversed());

        println("\nPossible sets listed by:  " + comparator);
        println("Minimum acceptable stats:" + mins.numbers() + "\n");

        var results = sets.stream()
            .filter(set -> set.dominates(mins))
            .limit(options.getLimit())
            .toList();

        for (int i = 0; i < results.size(); i++) {
            results.get(i).setName("Choice #" + (i + 1));
        }

        results.forEach(set -> {
            set.dump();
            println("");
        });
    }

    //-------------------------------------------------------------------------
    // Data Builders

    // Generates all possible armor sets from the available choices.
    private List<ArmorSet> generateSets() {
        var result = new ArrayList<ArmorSet>();

        for (var head = 0; head < vault.get(Type.HEAD).size(); head++) {
            for (var arms = 0; arms < vault.get(Type.ARMS).size(); arms++) {
                for (var body = 0; body < vault.get(Type.BODY).size(); body++) {
                    for (var legs = 0; legs < vault.get(Type.LEGS).size(); legs++) {
                        result.add(makeSet(head, arms, body, legs));
                    }
                }
            }
        }

        return result;
    }

    private ArmorSet makeSet(int head, int arms, int body, int legs) {
        var set = new ArmorSet();

        set.put(Type.HEAD, vault.get(Type.HEAD).get(head));
        set.put(Type.ARMS, vault.get(Type.ARMS).get(arms));
        set.put(Type.BODY, vault.get(Type.BODY).get(body));
        set.put(Type.LEGS, vault.get(Type.LEGS).get(legs));

        return set;
    }


    void dumpVault() {
        for (Type type : Type.values()) {
            for (Armor armor : vault.get(type)) {
                println(armor.data());
            }
        }
    }

    //-------------------------------------------------------------------------
    // File Parsing

    int lineNumber = 0;

    public List<Armor> readVault(String filename) throws AppError {
        var result = new ArrayList<Armor>();

        try {
            Files.lines(new File(filename).toPath())
                .map(line -> {
                    ++lineNumber;
                    return line.trim();
                })
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith("#"))
                .forEach(line -> result.add(parseArmor(lineNumber, line)));
        } catch (IOException ex) {
            throw new AppError("I/O Error reading data: " + ex.getMessage());
        }

        return result;
    }


    public Armor parseArmor(int lineNumber, String line) throws AppError {
        Scanner scanner = new Scanner(line).useDelimiter("\\s*,\\s*");
        try {
            var type = Type.valueOf(scanner.next());
            var name = scanner.next().trim();
            var armor = new Armor(type, name);

            Stat.stream().forEach(stat -> armor.put(stat, scanner.nextInt()));
            return armor;
        } catch (Exception ex) {
            throw new AppError("Line " + lineNumber + ", " + ex.getMessage());
        }
    }

    void println(String text) {
        System.out.println(text);
    }

    //-------------------------------------------------------------------------
    // Main
    public static void main(String[] args) {
        try {
            new ArmorApp().app(args);
        } catch (AppError ex) {
            System.out.println("Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}
