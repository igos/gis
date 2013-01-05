package pl.edu.pw.gis.convert;

import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
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
	public Graph convert(Graph graph) {
		
		Set<DefaultEdge> edges = graph.edgeSet();
		
		for (DefaultEdge e : edges) {
			if(graph.getEdgeWeight(e) > r) {
				graph.removeEdge(e);
			}			
		}
		return graph;
		//!TODO 
	}
}
