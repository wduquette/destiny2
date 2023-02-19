package destiny2;

public class Armor extends StatMap {
    private final Type type;
    private final Rarity rarity;
    private final String name;

    public Armor(Type type, Rarity rarity, String name) {
        this.type = type;
        this.rarity = rarity;
        this.name = name;
    }

    public Type type() {
        return type;
    }

    public Rarity rarity() {
        return rarity;
    }

    public boolean isExotic() {
        return rarity == Rarity.EXOTIC;
    }

    public String name() {
        return name;
    }

    public String data() {
        return String.format("%s %s %-30s %s", type, rarity, name, numbers());
    }

    public String toString() {
        return "Armor[" + data() + "]";
    }
}
