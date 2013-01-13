package pl.edu.pw.gis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import pl.edu.pw.gis.graph.GMLGraphBuilder;
import pl.edu.pw.gis.graph.Graph;

public class Cli {

	/**
	 * Entry point for Command Line interface
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Settings settings = parseCliArgs(args);
		if (settings == null) {
			// cannot go on like that. no settings!
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(160);
			// aaargh, cannot print on stderr...
			formatter.printHelp("gis.jar", buildCliOptions());
			System.exit(1);
		}
		// we got a nice settings object. let's read the file into some graph
		// object.

		long t0 = System.currentTimeMillis();
		System.out.println(timestamp(t0) + " | Loading data...");

		Graph<String, DefaultWeightedEdge> g = new Graph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		GMLGraphBuilder gb = new GMLGraphBuilder(g);
		try {
			gb.readGMLFile(settings.filePath);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("cannot parse provided GML file: "
					+ settings.filePath);
			System.exit(2);
		}
		// g should be filled now!
		if (settings.verbose)
			System.out.println("graph we have: " + g.toString());

		// some data structure, vertex -> reachable vertices
		CentralContainer centrals = new CentralContainer();
		// do the floyd-warshall thing
		FloydWarshallShortestPaths<String, DefaultWeightedEdge> fw = new FloydWarshallShortestPaths<String, DefaultWeightedEdge>(
				g);

		System.out.println(timestamp(t0)
				+ " | Graph loaded, starting computation...");

		for (String v : g.vertexSet()) {
			// for each vertex get shortest paths to all other vertices, and
			// remove vertices if path's length is > radius
			for (GraphPath<String, DefaultWeightedEdge> gp : fw
					.getShortestPaths(v)) {
				// pre-centrals relations
				if (settings.verbose) {
					System.out.println("|" + gp.getStartVertex() + ","
							+ gp.getEndVertex() + "| = " + gp.getWeight());
				}
				if (gp.getWeight() <= settings.radius) {
					// destination is a central for start edge
					centrals.put(gp.getStartVertex(), gp.getEndVertex());
				} else {
					// just put the vertex, no reachable central for now
					centrals.put(gp.getStartVertex());
				}
			}
		}
		System.out.println(timestamp(t0) + " | Potential centrals found.");
		if (settings.verbose)
			System.out.println(centrals.toString());
		// okay, in cetrals there are redundant centrals. step 2 is done. what
		// now?
		centrals.optimize();
		System.out.println(timestamp(t0) + " | Potential centrals optimized.");
		if (settings.verbose)
			System.out.println(centrals.toString());

		if (settings.test) {
			boolean result = CorrectnessChecker.check(centrals, g,
					settings.radius, fw);

			if (result) {
				System.out.println(timestamp(t0)
						+ " | brute-force test passed.");
			} else {
				System.out.println(timestamp(t0)
						+ " | brute-force test failed.");
			}
		}
		System.out.println(timestamp(t0) + " | Computation is done.");

		ArrayList<String> printCentrals = new ArrayList<String>();
		for (String s : centrals.getCentrals()) {
			printCentrals.add(s);
		}
		Collections.sort(printCentrals, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return Integer.parseInt(o1) - Integer.parseInt(o2);
			}

		});
		if (!settings.graphx) {
			System.out.println("Centrals found: " + printCentrals);
		} else {
			System.err.println("Visual result presentation not implemented");
		}
	}

	private static String timestamp(long t0) {
		long diff = System.currentTimeMillis() - t0;
		if (diff < 0) {
			diff = 0;
		}
		final long min = (diff / 60000);
		final long sec = (diff - min * 60000) / 1000;
		final long ms = diff - min * 60000 - sec * 1000;

		return String.format("+ %d m, %d s, %d ms", min, sec, ms);
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
		Settings settings = null;
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
				System.out.println("[I] r=" + settings.radius + ", file="
						+ settings.filePath);
			}
		}

		catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Error parsing input parameters: "
					+ exp.getMessage());
			settings = null;
		} catch (NumberFormatException ex) {
			System.err.println("Error parsing numbers: " + ex.getMessage());
			settings = null;
		}
		return settings;
	}
}
