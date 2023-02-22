package armory.types;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Sorts by the primary stats, in order, followed by the sum of the remainder.
 */
public class SuitComparator implements Comparator<Suit> {
    private final StatWeights weights;

    public SuitComparator(StatWeights weights) {
        this.weights = weights;
    }

    @Override
    public int compare(Suit set1, Suit set2) {
        return Double.compare(
            set1.weightedSum(weights),
            set2.weightedSum(weights));
    }

    @Override
    public String toString() {
        var list = new ArrayList<String>();

        for (var s : Stat.values()) {
            var weight = weights.get(s);
            if (weight == 1.0) {
                list.add(s.toString());
            } else if (weight != 0.0) {
                list.add(s + "*" + String.format("%03.1f", weight));
            }
        }
        return String.join(" + ", list);
    }
}
