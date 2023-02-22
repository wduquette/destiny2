/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package destiny2;

import java.io.File;
import java.util.Deque;

/**
 * A tool for building suits of armor
 */
public class BuildTool implements Tool {
    //-------------------------------------------------------------------------
    // Constructor

    public BuildTool() {
        // nothing to do
    }

    //-------------------------------------------------------------------------
    // The App

    @Override public String usage() {
        return "armory build <armory.dat> [options...]";
    }

    @Override public String oneLiner() {
        return "Builds suits of armor from the available pieces.";
    }

    @Override public String help() {
        return """
This tool builds possible suits of armor from the available pieces,
and ranks them according to the user's criteria.  All suits must
meet the user's desired minimum stats.

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
""";
    }

    @Override
    public void start(Deque<String> args) throws AppError {
        if (args.size() < 1) {
            println("Usage: " + usage());
            System.exit(1);
        }

        // NEXT, parse the command line
        var fileName = args.poll();
        var options = new Options(args);

        // FIRST, load the armor from the file.
        var db = new ArmorFile(new File(fileName));

        println("\nSuits from " + fileName + ":\n");
        db.getSuits().forEach(s -> {
            s.dump();
            println("");
        });

        // NEXT, get the suit to compare with.
        Suit current;

        if (options.getCompareWith() != null) {
            current = db.getSuits().stream()
                .filter(suit -> suit.getName().equals(options.getCompareWith()))
                .findFirst()
                .orElseThrow(() ->
                    new AppError("Unknown suit: " + options.getCompareWith()));
        } else {
            current = (!db.getSuits().isEmpty()) ? db.getSuits().get(0) : null;
        }

        // NEXT, generate the possible choices
        var suits = new Armory(db.getPieces()).allSuits();

        var comparator = new SuitComparator(options.getWeights());
        var mins = options.getMins();

        suits.sort(comparator.reversed());

        println("Number of possible suits:  " + suits.size());
        println("Possible suits ordered by: " + comparator);
        println("Minimum acceptable stats: " + mins.numbers());
        println("Comparing against suit:    " +
            (current != null ? current.getName() : "n/a"));
        println("");

        var results = suits.stream()
            .filter(set -> set.dominates(mins))
            .limit(options.getLimit())
            .toList();

        for (int i = 0; i < results.size(); i++) {
            results.get(i).setName("Choice #" + (i + 1));
        }

        results.forEach(set -> {
            if (current !=  null) {
                set.dumpComparison(current);
            } else {
                set.dump();
            }
            println("");
        });
    }
}
