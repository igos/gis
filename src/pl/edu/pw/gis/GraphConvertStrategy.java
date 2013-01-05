package pl.edu.pw.gis;

import org.jgrapht.graph.DefaultEdge;

import pl.edu.pw.gis.graph.Graph;

public interface GraphConvertStrategy {
	Graph<String,DefaultEdge> convert(Graph<String,DefaultEdge> graph);
}
