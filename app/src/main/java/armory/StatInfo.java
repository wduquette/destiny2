package armory;

public interface StatInfo {
    /**
     * Gets the value of the given stat.
     * @param stat The stat
     * @return the value
     */
    int stat(Stat stat);

    /**
     * Returns the sum of the stat values
     * @return The sum
     */
    int total();

    /**
     * Gets a weighted sum of the stat values, given the weights.
     * @param weights The stat weights
     * @return The sum
     */
    double weightedSum(StatWeights weights);

    /**
     * Computes the difference between the two sets of stats.
     * @param other The other stats
     * @return the difference
     */
    StatInfo diff(StatInfo other);

    /**
     * Returns true if every entry in this map is greater than or equal to
     * every entry in the other.
     * @param other The other map
     * @return true or false
     */
    boolean dominates(StatInfo other);

    /**
     * Gets the stats as row of numbers, with the sum.
     * @return The numbers.
     */
    String numbers();
}
