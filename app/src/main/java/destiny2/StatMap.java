package destiny2;

import java.util.HashMap;

public class StatMap extends HashMap<Stat,Integer> implements StatInfo {
    /**
     * Creates a default stat map with 0 values.
     */
    public StatMap() {
        Stat.forEach(s -> put(s, 0));
    }

    @Override
    public int stat(Stat stat) {
        return get(stat);
    }

    @Override
    public int total() {
        return values().stream().mapToInt(i -> i).sum();
    }

    @Override
    public double weightedSum(StatWeights weights) {
        return entrySet().stream()
            .mapToDouble(e -> weights.get(e.getKey()) * e.getValue())
            .sum();
    }

    @Override
    public StatInfo diff(StatInfo other) {
        var stats = new StatMap();
        Stat.forEach(s -> stats.put(s, stat(s) - other.stat(s)));
        return stats;
    }

    @Override
    public boolean dominates(StatInfo other) {
        for (var e : entrySet()) {
            if (e.getValue() < other.stat(e.getKey())) {
                return false;
            }
        }

        return true;
    }


    @Override
    public String numbers() {
        return String.format("%3d %3d %3d %3d %3d %3d = %4d",
            stat(Stat.MOB),
            stat(Stat.RES),
            stat(Stat.REC),
            stat(Stat.DIS),
            stat(Stat.INT),
            stat(Stat.STR),
            total()
        );
    }
}
