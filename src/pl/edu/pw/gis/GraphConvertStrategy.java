package pl.edu.pw.gis;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import pl.edu.pw.gis.graph.Graph;

public interface GraphConvertStrategy {
	Graph<String,DefaultWeightedEdge> convert(Graph<String,DefaultWeightedEdge> graph);
}
