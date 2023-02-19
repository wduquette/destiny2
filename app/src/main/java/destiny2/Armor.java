package destiny2;

public class Armor extends StatMap {
    private final Type type;
    private final String name;

    public Armor(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type type() {
        return type;
    }

    public String name() {
        return name;
    }

    public String data() {
        return String.format("%s %-30s %s", type, name, numbers());
    }

    public String toString() {
        return "Armor[" + data() + "]";
    }
}
