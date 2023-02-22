package destiny2;

import java.io.File;
import java.util.Deque;

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

    public String usage() {
        return "armory list <armory.dat>";
    }

    public String help() {
        return """
Outputs a list of the armor pieces defined in the file.
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
        var db = new ArmorFile(new File(fileName));

        System.out.println("\nPieces from " + fileName + ":\n");
        db.getPieces().forEach(p -> System.out.println(p.data()));
    }
}
