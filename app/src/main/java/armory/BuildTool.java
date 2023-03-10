/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package armory;

import armory.types.*;

import java.io.File;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * A tool for building suits of armor
 */
public class BuildTool implements Tool {
    //-------------------------------------------------------------------------
    // Instance Variables

    //
    // Option Values
    //

    // The maximum number of generated suits to display
    private int limit = 5;

    // The Suit of armor to compare the generated suits with.
    private String compareWith = null;

    // Minimum acceptable stat values
    private StatMap minStats;

    // Importance weights for each stat
    private StatWeights weights;

    private boolean listUnused = false;

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

The weight and minimum stat options override the weights and minimums
read from the armory file; if not in the armory file, they default to
1.0 and 0 respectively.

    -limit num     -- Maximum number of results to display, default is 5
    -unused        -- List the pieces of armor that aren't used in any
                      acceptable suit of armor
    -mob weight    -- The weight to put on the given stat.
    -res weight
    -rec weight
    -dis weight
    -int weight
    -str weight
    -minmob value  -- The minimum acceptable value for the stat
    -minres value
    -minrec value
    -mindis value
    -minint value
    -minstr value
""";
    }

    @Override
    public void start(Deque<String> args) throws AppError {
        if (args.size() < 1) {
            println("Usage: " + usage());
            System.exit(1);
        }

        // NEXT, get the armory from the file.
        var fileName = args.poll();
        var armory = new Armory(new File(fileName));
        minStats = armory.getMinStats();
        weights = armory.getWeights();

        // NEXT, parse the options.
        parseOptions(args);

        println("\nSuits from " + fileName + ":\n");
        armory.getSuits().forEach(s -> {
            s.dump();
            println("");
        });

        // NEXT, get the suit to compare with.
        Suit current;

        if (compareWith != null) {
            // TODO Can check compareWith when parsing options
            current = armory.getSuits().stream()
                .filter(suit -> suit.getName().equals(compareWith))
                .findFirst()
                .orElseThrow(() ->
                    new AppError("Unknown suit: " + compareWith));
        } else {
            current = armory.getSuits().stream().findFirst().orElse(null);
        }

        // NEXT, generate the possible choices
        var suits = armory.allSuits();

        var comparator = new SuitComparator(weights);

        suits.sort(comparator.reversed());

        println("Number of possible suits:  " + suits.size());
        println("Possible suits ordered by: " + comparator);
        println("Minimum acceptable stats: " + minStats.numbers());
        println("Comparing against suit:    " +
            (current != null ? current.getName() : "n/a"));
        println("");

        // NEXT, get and display the results
        var results = suits.stream()
            .filter(set -> set.dominates(minStats))
            .limit(limit)
            .toList();

        if (results.isEmpty()) {
            println("No acceptable suits found.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                results.get(i).setName("Choice #" + (i + 1));
            }

            results.forEach(set -> {
                if (current != null) {
                    set.dumpComparison(current);
                } else {
                    set.dump();
                }
                println("");
            });
        }

        // NEXT, build a set of pieces of armor that are not used in any
        // acceptable suit of armor.
        if (listUnused) {
            final var used = new HashSet<Armor>();

            results.forEach(suit -> used.addAll(suit.values()));

            var unused = armory.getPieces().stream()
                .filter(a -> !used.contains(a))
                .toList();

            if (!unused.isEmpty()) {
                println();
                println("The following pieces of armor are not used in any acceptable");
                println("suit of armor according to the current criteria.");
                println();

                unused.forEach(a -> println(a.data()));
            }
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
                case "-limit" ->
                    limit = requirePositiveInteger(opt, opts);
                case "-compare" ->
                    compareWith = requireString(opt, opts);
                case "-unused" ->
                    listUnused = true;
                case "-mob" ->
                    weights.put(Stat.MOB, requireWeight(opt, opts));
                case "-res" ->
                    weights.put(Stat.RES, requireWeight(opt, opts));
                case "-rec" ->
                    weights.put(Stat.REC, requireWeight(opt, opts));
                case "-dis" ->
                    weights.put(Stat.DIS, requireWeight(opt, opts));
                case "-int" ->
                    weights.put(Stat.INT, requireWeight(opt, opts));
                case "-str" ->
                    weights.put(Stat.STR, requireWeight(opt, opts));
                case "-minmob" ->
                    minStats.put(Stat.MOB, requirePositiveInteger(opt, opts));
                case "-minres" ->
                    minStats.put(Stat.RES, requirePositiveInteger(opt, opts));
                case "-minrec" ->
                    minStats.put(Stat.REC, requirePositiveInteger(opt, opts));
                case "-mindis" ->
                    minStats.put(Stat.DIS, requirePositiveInteger(opt, opts));
                case "-minint" ->
                    minStats.put(Stat.INT, requirePositiveInteger(opt, opts));
                case "-minstr" ->
                    minStats.put(Stat.STR, requirePositiveInteger(opt, opts));
                default ->
                    throw new AppError("Unknown option: " + opt);
            }
        }
    }


    //-------------------------------------------------------------------------
    // Helpers

    private String requireString(String opt, Deque<String> opts)
        throws AppError
    {
        if (opts.isEmpty()) {
            throw new AppError("Missing value for " + opt);
        }

        return opts.poll();
    }

    private int requirePositiveInteger(String opt, Deque<String> opts)
        throws AppError
    {
        var valueString = requireString(opt, opts);

        try {
            var value = Integer.parseInt(valueString);

            if (value < 0) {
                throw new AppError("Invalid " + opt + " value: " + valueString);
            }

            return value;
        } catch (Exception ex) {
            throw new AppError("Invalid " + opt + " value: " + valueString);
        }
    }

    private double requireWeight(String opt, Deque<String> opts)
        throws AppError
    {
        var valueString = requireString(opt, opts);

        try {
            var value = Double.parseDouble(valueString);

            if (value < 0) {
                throw new AppError("Invalid " + opt + " value: " + valueString);
            }

            return value;
        } catch (Exception ex) {
            throw new AppError("Invalid " + opt + " value: " + valueString);
        }
    }
}
