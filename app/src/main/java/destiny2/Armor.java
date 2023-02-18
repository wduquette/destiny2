package destiny2;

public class Armor extends StatVec {
    private final int index;
    private final Type type;
    private final String name;

    public Armor(int index, Type type, String name) {
        this.index = index;
        this.type = type;
        this.name = name;
    }

    public int index() {
        return index;
    }

    public Type type() {
        return type;
    }

    public String name() {
        return name;
    }

    public String data() {
        return String.format("%03d %s %-30s %s", index, type, name, numbers());
    }

    public String toString() {
        return "Armor[" + data() + "]";
    }
}
