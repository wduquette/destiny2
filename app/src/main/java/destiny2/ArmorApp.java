/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package destiny2;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;

public class ArmorApp {
    //-------------------------------------------------------------------------
    // Instance variables

    public final Map<String, Tool> tools = Map.of(
        "list", new ListTool(),
        "build", new BuildTool()
    );

    //-------------------------------------------------------------------------
    // The App

    /**
     * Gets the desired tool and executes it
     * @param argsArray The arguments from the command line.
     */
    public void app(String[] argsArray) {
        var args = new ArrayDeque<>(List.of(argsArray));

        if (args.isEmpty()) {
            println("Usage: armor <subcommand> [<arguments...>]");
            println("");
            println("Run \"armor help\" for a list of subcommands.");
            System.exit(1);
        }

        var subcommand = args.poll();
        var tool = tools.get(subcommand);

        if (tool == null) {
            println("Error, unrecognized subcommand: \"" + subcommand + "\"");
            println("");
            println("Run \"armor help\" for a list of subcommands.");
            System.exit(1);
        }

        tool.start(args);
    }

    //-------------------------------------------------------------------------
    // Data Functions

    void println(String text) {
        System.out.println(text);
    }

    //-------------------------------------------------------------------------
    // Main
    public static void main(String[] args) {
        try {
            new ArmorApp().app(args);
        } catch (AppError ex) {
            System.out.println("Error: " + ex.getMessage());
//            ex.printStackTrace(System.out);
        } catch (Exception ex) {
            System.out.println("Unexpected Exception Thrown: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}
