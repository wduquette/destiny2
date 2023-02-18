package destiny2;

import java.util.Comparator;
import java.util.List;

/**
 * Sorts by the primary stats, in order, followed by the sum of the remainder.
 */
public class ArmorComparator implements Comparator<ArmorSet> {
    private final List<Stat> primary;

    public ArmorComparator(Stat... primary) {
        this.primary = List.of(primary);
    }

    public ArmorComparator(List<Stat> primary) {
        this.primary = primary;
    }

    @Override
    public int compare(ArmorSet set1, ArmorSet set2) {
        var v1 = set1.stats();
        var v2 = set2.stats();
        var sum1 = primary.stream().mapToInt(v1::get).sum();
        var sum2 = primary.stream().mapToInt(v2::get).sum();

        return Integer.compare(sum1, sum2);
    }

    @Override
    public String toString() {
        return String.join("+", primary.stream().map(Stat::toString).toList());
    }
}
