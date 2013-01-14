package pl.edu.pw.gis;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import pl.edu.pw.gis.graph.GMLGraphBuilder;
import pl.edu.pw.gis.graph.Graph;

public class FindCentralsImpl implements Callable<FindCentralsImpl> {

	private final Settings settings;
	private Graph<String, DefaultWeightedEdge> graph;
	private CentralContainer centrals;

	public FindCentralsImpl(final Settings settings) {
		this.settings = settings;
	}

	public void run() {
		long t0 = System.currentTimeMillis();
		System.out.println(timestamp(t0) + " | Loading data...");

		graph = new Graph<String, DefaultWeightedEdge>(
				DefaultWeightedEdge.class);
		GMLGraphBuilder gb = new GMLGraphBuilder(graph);
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
			System.out.println("graph we have: " + graph.toString());

		centrals = new CentralContainer();
		// do the floyd-warshall thing
		FloydWarshallShortestPaths<String, DefaultWeightedEdge> fw = new FloydWarshallShortestPaths<String, DefaultWeightedEdge>(
				graph);

		System.out.println(timestamp(t0)
				+ " | Graph loaded, starting computation...");

		for (String v : graph.vertexSet()) {
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
			System.out.println(timestamp(t0)
					+ " | starting brute-force check.");
			boolean result = CorrectnessChecker.check(centrals, graph,
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

	}

	private static String timestamp(long t0) {
		long diff = System.currentTimeMillis() - t0;
		if (diff < 0) {
			diff = 0;
		}
		final long min = (diff / 60000);
		final long sec = (diff - min * 60000) / 1000;
		final long ms = diff - min * 60000 - sec * 1000;

		return String.format("%d m, %d s, %d ms", min, sec, ms);
	}

	@Override
	public FindCentralsImpl call() throws Exception {
		this.run();
		return this;
	}

	public Graph<String, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public CentralContainer getCentrals() {
		return centrals;
	}

}
