package armory;

import armory.types.Armor;
import armory.types.Type;

import java.io.File;
import java.util.Deque;
import java.util.HashSet;

/**
 * A tool to list the pieces of armor in the file.
 */
public class ListTool implements Tool {
    //-------------------------------------------------------------------------
    // Constructor

    public ListTool() {
        // Nothing to do
    }

    //-------------------------------------------------------------------------
    // Application Code

    @Override public String usage() {
        return "armory list <armory.dat>";
    }

    @Override public String oneLiner() {
        return "Lists the available pieces of armor.";
    }

    @Override public String help() {
        return """
Outputs a list of the armor pieces defined in the armory file.
""";
    }

    /**
     * Invokes the tool given the arguments.
     *
     * @param args The command line arguments for this tool
     */
    public void start(Deque<String> args) {
        if (args.size() != 1) {
            System.out.println("Usage: " + usage());
            System.exit(1);
        }

        var fileName = args.poll();

        // FIRST, load the armor from the file.
        var db = new Armory(new File(fileName));

        System.out.println("\nPieces from " + fileName + ":\n");
        db.getPieces().forEach(p -> System.out.println(p.data()));

        // NEXT, look for dominated pieces
        var dominated = new HashSet<Armor>();

        var typeLists = Armory.getTypeLists(db.getPieces());

        for (var list : typeLists.values()) {
            for (var a1 : list) {
                for (var a2 : list) {
                    if (a1 != a2 && a1.dominates(a2)) {
                        dominated.add(a2);
                    }
                }
            }
        }

        if (!dominated.isEmpty()) {
            println();
            println("The following pieces of armor are dominated by other pieces of");
            println("the same type.  You might wish to dispose of them.");
            println();

            dominated.forEach(a -> println(a.data()));
        }
    }
}
