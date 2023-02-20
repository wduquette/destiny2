package destiny2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of armor, by type.
 */
public class Vault extends HashMap<Type, List<Armor>> {
    /**
     * Creates a new vault out of the given pieces
     * @param pieces The pieces of armor.
     */
    public Vault(List<Armor> pieces) {
        pieces.forEach(piece -> {
            putIfAbsent(piece.type(), new ArrayList<>());
            get(piece.type()).add(piece);
        });
    }

    /**
     * Get the number of pieces of the given type in the vault
     * @param type The type
     * @return The number of pieces.
     */
    public int size(Type type) {
        return get(type).size();
    }

    /**
     * Gets the indicated piece of the given type.
     * @param type The type
     * @param index The index of the piece
     * @return The piece
     */
    public Armor get(Type type, int index) {
        return get(type).get(index);
    }

    /**
     * Make the armor set for the indicated pieces of armor
     * @param head Index of the head piece
     * @param arms Index of the arms piece
     * @param body Index of the body piece
     * @param legs Index of the legs piece
     * @return The set
     */
    public ArmorSet makeSet(int head, int arms, int body, int legs) {
        var set = new ArmorSet();

        set.put(Type.HEAD, get(Type.HEAD).get(head));
        set.put(Type.ARMS, get(Type.ARMS).get(arms));
        set.put(Type.BODY, get(Type.BODY).get(body));
        set.put(Type.LEGS, get(Type.LEGS).get(legs));

        return set;
    }

    /**
     * Gets all valid sets of armor for the pieces in this vault.
     * @return The list of sets.
     */
    public List<ArmorSet> allSets() {
        var result = new ArrayList<ArmorSet>();

        // Is there a cleaner, more concise way to do this?
        for (var head = 0; head < size(Type.HEAD); head++) {
            for (var arms = 0; arms < size(Type.ARMS); arms++) {
                for (var body = 0; body < size(Type.BODY); body++) {
                    for (var legs = 0; legs < size(Type.LEGS); legs++) {
                        // If there are two or more exotics, it isn't a valid
                        // armor set.
                        var set = makeSet(head, arms, body, legs);

                        var numberOfExotics = set.values().stream()
                            .filter(Armor::isExotic)
                            .count();

                        if (numberOfExotics <= 1) {
                            result.add(set);
                        }
                    }
                }
            }
        }

        return result;
    }

}
