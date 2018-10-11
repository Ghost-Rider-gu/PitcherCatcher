package software.ping.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import software.ping.core.Catcher;
import software.ping.core.Pitcher;

/**
 * Parse command line and prepare pitcher or catcher for start.
 */
public class PitcherCatcherStarter {

    private CommandLineParams params;
    private Options options;

    /**
     * Constructor.
     */
    public PitcherCatcherStarter() {
        this.params = new CommandLineParams();
        this.options = new Options();

        options.addOption("h", "help", false, "Show help");
        options.addOption("c", "catcher", false, "Launch as a Catcher");
        options.addOption("p", "pitcher", false, "Launch as a Pitcher");
        options.addOption("bind", "bind", true, "TCP socket bind address that will be used to run listen");
        options.addOption("port", "port", true, "TCP socket port used for connecting or listening");
        options.addOption("size", "size", true, "Message length (minimum: 50, maximum: 3000, default: 300)");
        options.addOption("mps", "mps", true, "The speed of message sending expressed as 'messages per second' (default: 1)");
        options.addOption("host", "hostname", true, "The name of the computer which runs Catcher");
    }

    /**
     * Parse command line and prepare to start Pitcher or Catcher (depends on the first parameter).
     *
     * @param arguments {@link String[]}
     */
    public void prepareToStart(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            this.showHelp();
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, arguments);

            if (commandLine.hasOption("h")) {
                this.showHelp();
            }

            // for catcher application
            if (commandLine.hasOption("c")) {
                if (commandLine.hasOption("bind")) {
                    this.params.setHostName(commandLine.getOptionValue("bind"));
                } else {
                    throw new ParseException("You need to point a bind parameter");
                }

                if (commandLine.hasOption("port")) {
                    this.params.setPort(Integer.parseInt(commandLine.getOptionValue("port")));
                } else {
                    throw new ParseException("You need to point a port parameter");
                }

                Catcher catcherApp = new Catcher(this.params);
                catcherApp.startCatcher();
            }

            // for pitcher application
            if (commandLine.hasOption("p")) {
                if (commandLine.hasOption("port")) {
                    this.params.setPort(Integer.parseInt(commandLine.getOptionValue("port")));
                } else {
                    throw new ParseException("You need to point a port parameter");
                }

                if (commandLine.hasOption("mps")) {
                    this.params.setMps(Integer.parseInt(commandLine.getOptionValue("mps")));
                }

                if (commandLine.hasOption("size")) {
                    int messageLength = Integer.parseInt(commandLine.getOptionValue("size"));
                    if (messageLength < 50 || messageLength > 3000) {
                        throw new ParseException("Inappropriate length for message. Message length should be from 50 to 3000.");
                    } else {
                        this.params.setMessageLength(Integer.parseInt(commandLine.getOptionValue("size")));
                    }
                }

                if (commandLine.hasOption("host")) {
                    this.params.setHostName(commandLine.getOptionValue("host"));
                } else {
                    throw new ParseException("You need to point a host parameter");
                }

                Pitcher pitcherApp = new Pitcher(this.params);
                pitcherApp.startPitcher();
            }

        } catch (ParseException ex) {
            System.out.println("Failed to parse command line: " + ex.getMessage());
            System.exit(0);
        }
    }

    /**
     * Show help.
     */
    private void showHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("HELP", options);

        System.exit(0);
    }

}
