package destiny2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * The user's input options.
 */
public class Options {
    private final String fileName;
    private int limit = 5;
    private final StatMap mins = new StatMap();
    private final StatWeights weights = new StatWeights();

    /**
     * Parses the options and makes them available to the application.
     * @param args The command line arguments.
     * @throws AppError On input error
     */
    public Options(String[] args) throws AppError {
        var opts = new ArrayDeque<>(List.of(args));

        this.fileName = opts.poll();

        while (!opts.isEmpty()) {
            var opt = opts.poll();

            if (!opt.startsWith("-")) {
                throw new AppError("Expected an option: " + opt);
            }

            switch (opt) {
                case "-limit" ->
                    limit = requirePositiveInteger(opt, opts);
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
                    mins.put(Stat.MOB, requirePositiveInteger(opt, opts));
                case "-minres" ->
                    mins.put(Stat.RES, requirePositiveInteger(opt, opts));
                case "-minrec" ->
                    mins.put(Stat.REC, requirePositiveInteger(opt, opts));
                case "-mindis" ->
                    mins.put(Stat.DIS, requirePositiveInteger(opt, opts));
                case "-minint" ->
                    mins.put(Stat.INT, requirePositiveInteger(opt, opts));
                case "-minstr" ->
                    mins.put(Stat.STR, requirePositiveInteger(opt, opts));
                default ->
                    throw new AppError("Unknown option: " + opt);
            }
        }
    }

    /**
     * Gets the armor data file's name, relative to the current working
     * directory.
     * @return The name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the maximum number of possible armor sets to list.
     * @return The number
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the minimum acceptable value for each stat.
     * @return The minimums
     */
    public StatMap getMins() {
        return mins;
    }

    /**
     * Gets a weights to use when ordering the armor sets.
     * @return The weights
     */
    public StatWeights getWeights() {
        return weights;
    }

    //-------------------------------------------------------------------------
    // Helpers

    private int requirePositiveInteger(String opt, Deque<String> opts)
        throws AppError {
        if (opts.isEmpty()) {
            throw new AppError("Missing value for " + opt);
        }

        var valueString = opts.poll();
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
        throws AppError {
        if (opts.isEmpty()) {
            throw new AppError("Missing value for " + opt);
        }

        var valueString = opts.poll();
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
