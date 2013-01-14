package pl.edu.pw.gis;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jgrapht.graph.DefaultWeightedEdge;

import pl.edu.pw.gis.graph.Graph;

public class GraphGenenerator {
	private static int vertices = 0;
	private static double probability = 0.0;
	private static double weight = 0.0;
	private static String file = null;

	/**
	 * Call this stuff to generate GML file with flat undirected graph of v
	 * vertices and with probability of edge of p
	 * 
	 * @param args
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption(OptionBuilder
				.withArgName("vertices")
				.withDescription(
						"number of vertices in graph, up to "
								+ Integer.MAX_VALUE).hasArg().isRequired()
				.withLongOpt("vertices").create("v"));
		options.addOption(OptionBuilder.withArgName("probability")
				.withDescription("probability of edge existance, eg. 0.03")
				.hasArg().isRequired().withLongOpt("probability").create("p"));
		options.addOption(OptionBuilder.withArgName("max-weight")
				.withDescription("max weight of edge, eg. 8.5").hasArg()
				.isRequired().withLongOpt("max-weight").create("w"));
		options.addOption(OptionBuilder.withArgName("file")
				.withDescription("output file").hasArg().withLongOpt("file")
				.create("f"));

		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, args);
			vertices = Integer.parseInt(line.getOptionValue("v"));
			probability = Double.parseDouble(line.getOptionValue("p"));
			weight = Double.parseDouble(line.getOptionValue("w"));
			if (line.hasOption("f")) {
				file = line.getOptionValue("f");
			}
		} catch (ParseException exp) {
			System.err.println("Error parsing input parameters: "
					+ exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("genGraph.jar", options);
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.err.println("Error parsing numbers: " + ex.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("genGraph.jar", options);
			System.exit(2);
		}
		System.out.println("Generating graph with " + vertices
				+ " vertices, with edge probability of " + probability + "...");
		System.out.println("max heap size: " + Runtime.getRuntime().maxMemory()/1024/1024 + "MB");
		
		// do the generation
		Random r = new Random();
		Graph<String, DefaultWeightedEdge> g = new Graph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		// String[] vertexArray = new String[(int) vertices];
		for (int i = 0; i < vertices; i++) {
			g.addVertex("" + i);
			// vertexArray[i] = "" + i;
		}
		int left = 0;
		for (int i = 0; i < vertices * (vertices - 1) * probability / 2 ; i++) {
			try {
				left += r.nextInt(new Double(29.0 * vertices * (left + 1)/vertices).intValue());
				left %= (vertices - 3);
			g.addWeightedEdge("" + left,
					"" + (left + r.nextInt(vertices - left) + 1), Math.round( r.nextDouble() * weight * 100.0 ) / 100.0);
			} catch (IllegalArgumentException e) {
//				System.err.println("WARN: " + e.getMessage());
				// loops are not allowed
				i--;
			} catch (NullPointerException e) {
//				System.err.println("WARN: " + e.getMessage());
				// such edge must have existed, a pity..
				i--; //we need another vertex
			}
			if (i % 100000 == 0)
				System.out.print("\r\\e[2K" + (Math.round( i * 100 / (vertices * (vertices - 1) * probability / 2) )) + "%");
			// vertexArray[i] = "" + i;
		}
		System.out.println("Done! Writing to " + (file==null ? "screen" : "file " + file) + "...");

		PrintStream writer = null;
		if (file == null) {
			writer = System.out;
		}
		else {
			File f = new File(file);
			try {
				f.createNewFile();
				writer = new PrintStream(f);
			} catch (IOException e) {
				System.err.println("ooops..." + e.getMessage());
				e.printStackTrace();
			}
		}

		writer.println("graph [");
		writer.println("\tdirected 0");

		for (String v : g.vertexSet()) {
			writer.println("\tnode [");
			writer.println("\t\tid " + v);
			writer.println("\t\tlabel \"v" + v + "\"");
			writer.println("\t]");
		}

		for (DefaultWeightedEdge v : g.edgeSet()) {
			writer.println("\tedge [");
			writer.println("\t\tsource " + g.getEdgeSource(v));
			writer.println("\t\ttarget " + g.getEdgeTarget(v));
			writer.println("\t\tlabel " + "\"w" + g.getEdgeWeight(v) + "\"");
			writer.println("\t\tweight " + g.getEdgeWeight(v));
			writer.println("\t]");
		}

		writer.println("]");
		writer.flush();
		if (file != null) {
			writer.close();
		}
		
		System.out.println("All done!");
	}

}
