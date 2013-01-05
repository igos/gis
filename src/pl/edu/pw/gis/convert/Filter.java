package pl.edu.pw.gis.convert;

import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import pl.edu.pw.gis.GraphConvertStrategy;
import pl.edu.pw.gis.graph.Graph;

/**
 * Filtruje krawedzie zostawiajac tyl te ktorych odleglosci sa mniejsze od R
 * @author Igor
 *
 */
public class Filter implements GraphConvertStrategy {
	private double r;
	
	public Filter(double r) {
		this.r = r;
	}
	
	@Override
	public Graph<String,DefaultWeightedEdge> convert(Graph<String,DefaultWeightedEdge> graph) {
		
		Set<DefaultWeightedEdge> edges = graph.edgeSet();
		
		for (DefaultWeightedEdge e : edges) {
			if(graph.getEdgeWeight(e) > r) {
				graph.removeEdge(e);
			}			
		}
		return graph;
		//!TODO usuwanie wierzcholkow bez krawedzi???
	}
}
