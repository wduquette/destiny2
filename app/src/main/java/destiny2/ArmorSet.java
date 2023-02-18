package destiny2;

import java.util.HashMap;

public class ArmorSet extends HashMap<Type,Armor> {

    public StatVec stats() {
        var vec = new StatVec();

        values().forEach(armor -> {
            Stat.stream().forEach(stat -> {
                int value = vec.getOrDefault(stat, 0);
                vec.put(stat, value + armor.get(stat));
            });
        });

        return vec;
    }

    public String numbers() {
        return stats().numbers();
    }

    public void dump() {
        var header = String.format("%-41s %s", "Armor Set", numbers());
        System.out.println(header);

        for (var type : Type.values()) {
            System.out.println("  " + get(type).data());
        }
    }
}
