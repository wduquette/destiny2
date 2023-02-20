package destiny2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A collection of armor, by type.
 */
@SuppressWarnings("unused")
public class Armory extends HashMap<Type, List<Armor>> {
    /**
     * Creates a new armory out of the given pieces
     * @param pieces The pieces of armor.
     */
    public Armory(List<Armor> pieces) {
        pieces.forEach(piece -> {
            putIfAbsent(piece.type(), new ArrayList<>());
            get(piece.type()).add(piece);
        });
    }

    /**
     * Get the number of pieces of the given type in the armory
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
     * Make the suit for the indicated pieces of armor
     * @param head Index of the head piece
     * @param arms Index of the arms piece
     * @param body Index of the body piece
     * @param legs Index of the legs piece
     * @return The suit
     */
    public Suit makeSuit(int head, int arms, int body, int legs) {
        var suit = new Suit();

        suit.put(Type.HEAD, get(Type.HEAD).get(head));
        suit.put(Type.ARMS, get(Type.ARMS).get(arms));
        suit.put(Type.BODY, get(Type.BODY).get(body));
        suit.put(Type.LEGS, get(Type.LEGS).get(legs));

        return suit;
    }

    /**
     * Gets all valid suits of armor for the pieces in this armory.
     * @return The list of suits.
     */
    public List<Suit> allSuits() {
        var result = new ArrayList<Suit>();

        // Is there a cleaner, more concise way to do this?
        for (var head = 0; head < size(Type.HEAD); head++) {
            for (var arms = 0; arms < size(Type.ARMS); arms++) {
                for (var body = 0; body < size(Type.BODY); body++) {
                    for (var legs = 0; legs < size(Type.LEGS); legs++) {
                        // If there are two or more exotics, it isn't a valid
                        // suit
                        var suit = makeSuit(head, arms, body, legs);

                        var numberOfExotics = suit.values().stream()
                            .filter(Armor::isExotic)
                            .count();

                        if (numberOfExotics <= 1) {
                            result.add(suit);
                        }
                    }
                }
            }
        }

        return result;
    }

}
