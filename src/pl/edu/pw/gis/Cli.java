package pl.edu.pw.gis;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {

	/**
	 * Entry point for Command Line interface
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Settings settings = parseCliArgs(args);
		if (settings == null) {
			//cannot go on like that. no settings!
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(160);
			// aaargh, cannot print on stderr...
			formatter.printHelp("gis.jar", buildCliOptions());
			System.exit(1);
		}
		// we got a nice settings object. let's read the file into some graph object.

	}

	@SuppressWarnings("static-access")
	private static Options buildCliOptions() {
		// create Options object
		Options options = new Options();
		// add t option
		options.addOption("l", true,
				"time limit for finding solution, in seconds. Default: no limit");
		options.addOption("v", false, "be more verbose");
		options.addOption("t", false,
				"launch accurate tests to verify the solution. May be time consuming!");
		options.addOption("x", false, "present solution in graphical way");

		options.addOption(OptionBuilder
				.withArgName("radius")
				.withDescription(
						"max distance from any vertex to the closest central")
				.hasArg().isRequired().withLongOpt("radius").create("r"));
		options.addOption(OptionBuilder
				.withArgName("limit")
				.withDescription(
						"time limit for finding solution, in seconds. Default: no limit")
				.hasArg().withLongOpt("limit").create("l"));
		options.addOption(OptionBuilder.withArgName("file")
				.withDescription("path to GML file with graph definition")
				.hasArg().isRequired().withType(String.class)
				.withLongOpt("file").create("f"));

		return options;
	}

	public static Settings parseCliArgs(String[] args) {

		Options options = buildCliOptions();
		Settings settings= null;
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			settings = new Settings();
			settings.filePath = line.getOptionValue("f");
			settings.radius = Long.parseLong(line.getOptionValue("r"));
			settings.verbose = line.hasOption("v");
			settings.test = line.hasOption("t");
			settings.graphx = line.hasOption("x");
			settings.limit = -1;

			if (line.hasOption("l")) {
				settings.limit = Long.parseLong(line.getOptionValue("l"));
			}
			if (settings.verbose) {
				System.out.println("[I] Input parameters parsed successfully:");
				System.out.println("[I] r=" + settings.radius +", file=" + settings.filePath);
			}
		}

		catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Error parsing input parameters: "
					+ exp.getMessage());
			settings = null;
		}
		catch (NumberFormatException ex) {
			System.err.println("Error parsing numbers: "
					+ ex.getMessage());
			settings = null;
		}
		return settings;
	}
}
