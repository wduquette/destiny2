package destiny2;

import java.util.HashMap;

/**
 * A collection of stats.
 */
public class StatVec extends HashMap<Stat,Integer> {
    public String numbers() {
        return String.format(" %2d  %2d  %2d  %2d  %2d  %2d",
            get(Stat.MOB),
            get(Stat.RES),
            get(Stat.REC),
            get(Stat.DIS),
            get(Stat.INT),
            get(Stat.STR)
        );
    }
}
