package armory.types;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Armor armor = (Armor) o;

        if (type != armor.type) return false;
        if (rarity != armor.rarity) return false;
        return name.equals(armor.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + rarity.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
