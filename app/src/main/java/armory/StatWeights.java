package armory;

import java.util.HashMap;

/**
 * A dictionary of weights for each stat.
 */
public class StatWeights extends HashMap<Stat,Double> {
    /**
     * Creates a default object with all weights = 1.0.
     */
    public StatWeights() {
        Stat.forEach(stat -> put(stat, 1.0));
    }
}
