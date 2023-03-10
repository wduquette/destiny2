/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package armory;

import armory.types.AppError;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;

public class ArmoryApp {
    //-------------------------------------------------------------------------
    // Instance variables

    public final static Map<String, Tool> TOOLS = Map.of(
        "list",   new ListTool(),
        "build",  new BuildTool(),
        "import", new ImportTool(),
        "help",   new HelpTool()
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
            println("Usage: armory <subcommand> [<arguments...>]");
            println("");
            println("Run \"armory help\" for a list of subcommands.");
            System.exit(1);
        }

        var subcommand = args.poll();
        var tool = TOOLS.get(subcommand);

        if (tool == null) {
            println("Error, unrecognized subcommand: \"" + subcommand + "\"");
            println("");
            println("Run \"armory help\" for a list of subcommands.");
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
            new ArmoryApp().app(args);
        } catch (AppError ex) {
            System.out.println("Error: " + ex.getMessage());
//            ex.printStackTrace(System.out);
        } catch (Exception ex) {
            System.out.println("Unexpected Exception Thrown: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}
