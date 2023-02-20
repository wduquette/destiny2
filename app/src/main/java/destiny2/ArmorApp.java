/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package destiny2;

import java.io.File;

public class ArmorApp {
    //-------------------------------------------------------------------------
    // Instance variables

    // The data loaded from the armor file.
    private ArmorFile db;

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
        db = new ArmorFile(new File(options.getFileName()));

        println("\nPieces from " + options.getFileName() + ":\n");
        db.getPieces().forEach(p ->
            System.out.printf("%04d %s\n", db.getLineNumber(p), p.data()));

        println("\nSuits from " + options.getFileName() + ":\n");
        db.getSuits().forEach(s -> {
            s.dump();
            println("");
        });

        // NEXT, generate the possible choices
        var vault = new Vault(db.getPieces());
        var sets = vault.allSets();

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
    // Data Functions

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
            ex.printStackTrace(System.out);
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}
