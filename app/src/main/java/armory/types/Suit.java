package armory.types;

import java.util.HashMap;

/**
 * A complete suit of armor: HEAD, ARMS, BODY, LEGS.
 */
public class Suit extends HashMap<Type, Armor> implements StatInfo {
    //------------------------------------------------------------------------
    // Instance Variables

    private String name = "Suit";

    //------------------------------------------------------------------------
    // Constructor

    public Suit() {
        Type.forEach(t -> put(t, new Armor(t, Rarity.LEGEND, "none")));
    }

    public Suit(Armor head, Armor arms, Armor body, Armor legs) {
        put(Type.HEAD, head);
        put(Type.ARMS, arms);
        put(Type.BODY, body);
        put(Type.LEGS, legs);
    }

    //------------------------------------------------------------------------
    // Suit API

    /**
     * Get the suit name
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the suit name
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }


    //------------------------------------------------------------------------
    // Stats API

    @Override
    public int stat(Stat stat) {
        return stats().stat(stat);
    }

    @Override
    public int total() {
        return stats().total();
    }

    @Override
    public double weightedSum(StatWeights weights) {
        return stats().weightedSum(weights);
    }

    @Override
    public boolean dominates(StatInfo other) {
        return stats().dominates(other);
    }

    @Override
    public StatInfo diff(StatInfo other) {
        return stats().diff(other);
    }

    @Override
    public String numbers() {
        return stats().numbers();
    }

    // Gets the total stats
    private StatInfo stats() {
        var vec = new StatMap();

        values().forEach(armor -> {
            for (var e : armor.entrySet()) {
                vec.put(e.getKey(), e.getValue() + vec.get(e.getKey()));
            }
        });

        return vec;
    }

    public String data() {
        return String.format("%-44s %s", name, numbers());
    }

    public void dump() {
        System.out.println(data());
        Type.forEach(type -> System.out.println("  " + get(type).data()));
    }

    public void dumpComparison(Suit other) {
        System.out.println(data() + "  -- " + diff(other).numbers());
        Type.forEach(type -> {
            var piece1 = get(type);
            var piece2 = other.get(type);
            System.out.println("  " + piece1.data()
                + "  -- " + piece1.diff(piece2).numbers());
        });

    }
}
