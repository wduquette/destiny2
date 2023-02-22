package armory;

import armory.types.AppError;
import armory.types.Stat;
import armory.types.StatMap;
import armory.types.StatWeights;

import java.util.Deque;

/**
 * The user's input options.
 */
public class Options {
    private String compareWith = null;
    private int limit = 5;
    private final StatMap mins = new StatMap();
    private final StatWeights weights = new StatWeights();

    /**
     * Parses the options and makes them available to the application.
     * @param opts The command line options
     * @throws AppError On input error
     */
    public Options(Deque<String> opts) throws AppError {
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
     * Gets the maximum number of possible armor sets to list.
     * @return The number
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the name of the armor set to compare with.
     * @return The set.
     */
    public String getCompareWith() {
        return compareWith;
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
