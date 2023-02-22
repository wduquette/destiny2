package destiny2;

import java.util.Deque;

/**
 * A tool to provide help to the user
 */
public class HelpTool implements Tool {
    //-------------------------------------------------------------------------
    // Constructor

    public HelpTool() {
        // Nothing to do
    }

    //-------------------------------------------------------------------------
    // Tool Definition

    @Override public String usage() {
        return "armory help [<subcommand>]";
    }

    @Override public String oneLiner() {
        return "Displays help for the application's subcommands.";
    }

    @Override public String help() {
        return oneLiner();
    }

    /**
     * Invokes the tool given the arguments.
     *
     * @param args The command line arguments for this tool
     */
    public void start(Deque<String> args) {
        if (args.isEmpty()) {
            printHelpList();
        } else if (args.size() == 1) {
            printToolHelp(args.poll());
        } else {
            println("Usage: " + usage());
        }
    }

    private void printHelpList() {
        println("Usage: armory <subcommand> [<arguments...>]");
        println();

        ArmoryApp.TOOLS.forEach((key, value) ->
            printf("  %-8s  %s\n", key, value.oneLiner()));
    }

    private void printToolHelp(String name) {
        var tool = ArmoryApp.TOOLS.get(name);

        if (tool == null) {
            println("Unknown tool: \"" + name + "\"\n");
            printHelpList();
        } else {
            println(tool.usage());
            println();
            println(tool.help());
        }
    }
}
