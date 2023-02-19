package destiny2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Sorts by the primary stats, in order, followed by the sum of the remainder.
 */
public class ArmorComparator implements Comparator<ArmorSet> {
    private final StatWeights weights;

    public ArmorComparator(StatWeights weights) {
        this.weights = weights;
    }

    @Override
    public int compare(ArmorSet set1, ArmorSet set2) {
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
