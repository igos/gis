package pl.edu.pw.gis;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jgrapht.graph.DefaultWeightedEdge;

import pl.edu.pw.gis.graph.JGraphXAdapter;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;

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

		RunnableFuture<FindCentralsImpl> rf = new FutureTask<FindCentralsImpl>(
				new FindCentralsImpl(settings));
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(rf);
		FindCentralsImpl result = null;

		try {
			if (settings.limit > 0)
				result = rf.get(settings.limit, TimeUnit.SECONDS);
			else
				result = rf.get();
		} catch (TimeoutException te) {
			// computation did not made it...
			rf.cancel(true);
			System.out
					.println("[!!] Timeout of "
							+ settings.limit
							+ "s reached. Computation abandoned. Progam will exit now.");
			System.exit(3);
		}
		catch (OutOfMemoryError e ) {
			System.out.println("[!!] Program run out of memory, increase heap size (-Xmx size) and try again");
			System.exit(4);
		}
		catch (Exception e) {
			// Evil, unhandled exception!
			System.out.println("[!!] System error: " + e.getMessage());
//			e.printStackTrace();
			System.exit(666);
		}
		finally {
			es.shutdown();
		}

		if (!settings.graphx) {
			ArrayList<String> printCentrals = new ArrayList<String>();
			for (String s : result.getCentrals().getCentrals()) {
				printCentrals.add(s);
			}
			Collections.sort(printCentrals, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return Integer.parseInt(o1) - Integer.parseInt(o2);
				}

			});
			System.out.println("Centrals found: " + printCentrals);
		} else {
			//display
			System.out.print(result.getGraph());
			
			final JGraphXAdapter<String, DefaultWeightedEdge> graph = result.getGraph().getXGraph();
	        
	        JFrame frame = new JFrame();
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(600, 600);
	        
	        mxGraphComponent graphComponent = new mxGraphComponent(graph);
	        frame.getContentPane().add(BorderLayout.CENTER, graphComponent);
	        frame.setVisible(true);
	        
	        graph.getModel().beginUpdate();
	        double x = 5, y = 5;
	        for (mxCell cell : graph.getVertexToCellMap().values()) {
	            x = 10 + (int)(Math.random() * ((500 - 10) + 1));
	            y = 10 + (int)(Math.random() * ((500 - 10) + 1));
	        		            
	        	graph.getModel().setGeometry(cell, new mxGeometry(x, y, 20, 20));
	        	
	        	if(result.getCentrals().isCentral(cell.getId())) {
	        		graph.getModel().setStyle(cell, "defaultVertex;fillColor=red");
	        	}
	        }
	        graph.getModel().endUpdate();
		}

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
				if (settings.limit <= 0) {
					System.out.println("Time limit must be > 0. Exitting...");
					System.exit(6);
				}
			}
			if (settings.verbose) {
				System.out.println("[I] Input parameters parsed successfully:");
				System.out.println("[I] r=" + settings.radius + ", file="
						+ settings.filePath);
			}
			if (settings.radius <= 0) {
				System.out.println("Radius must be > 0. Exitting...");
				System.exit(5);
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
