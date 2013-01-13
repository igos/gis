package pl.edu.pw.gis;

import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import pl.edu.pw.gis.graph.Graph;

public class CorrectnessChecker {
	/**
	 * Check the correctness of centrals container
	 * @param centrals - container with centrals
	 * @param g - original graph
	 * @param radius - max distance from vertex to central
	 * @param fw - floyd-warshall shortest paths collection
	 * @return true if basic assumptions are checked
	 */
	public static boolean check(CentralContainer centrals,
			Graph<String, DefaultWeightedEdge> g, long radius,
			FloydWarshallShortestPaths<String, DefaultWeightedEdge> fw) {
		// for each vertex check, if distance to any of centrals is smaller or
		// equal radius
		boolean result = true; // let's be optimistic
		for (String v : g.vertexSet()) {
			boolean perVertex = false;
			for (String c : centrals.getCentrals()) {
				// v must be a central or has a central nearby
				if (centrals.isCentral(v) || fw.getShortestPath(v, c).getWeight() <= radius) {
					perVertex = true;
					break;
				}
			}
			// all centrals checked, is some nearby one found?
			if (!perVertex) {
				System.err.println("Vertex " + v
						+ " has no central within " + radius + " distance");
				result = false;
				break;
			}
		}
		return result;
	}
}
