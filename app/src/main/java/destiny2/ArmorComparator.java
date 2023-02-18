package destiny2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Sorts by the primary stats, in order, followed by the sum of the remainder.
 */
public class ArmorComparator implements Comparator<ArmorSet> {
    private final Map<Stat,Double> weights;

    public ArmorComparator(Map<Stat,Double> weights) {
        this.weights = weights;
    }

    @Override
    public int compare(ArmorSet set1, ArmorSet set2) {
        var stats1 = set1.stats();
        var stats2 = set2.stats();
        var sum1 = 0.0;
        var sum2 = 0.0;

        for (var s : Stat.values()) {
            sum1 += stats1.get(s)*weights.get(s);
            sum2 += stats2.get(s)*weights.get(s);
        }

        return Double.compare(sum1, sum2);
    }

    @Override
    public String toString() {
        var list = new ArrayList<String>();

        for (var s : Stat.values()) {
            list.add(s + "=" + String.format("%03.1f", weights.get(s)));
        }
        return String.join(", ", list);
    }
}
