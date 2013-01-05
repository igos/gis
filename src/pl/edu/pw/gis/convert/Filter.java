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
	public Graph<String,DefaultEdge> convert(Graph<String,DefaultEdge> graph) {
		
		Set<DefaultEdge> edges = graph.edgeSet();
		
		graph.addEdge(arg0, arg1, arg2)
		
		for (DefaultEdge e : edges) {
			if(graph.getEdgeWeight(e) > r) {
				graph.removeEdge(e);
			}			
		}
		return graph;
		//!TODO usuwanie wierzcholkow bez krawedzi???
	}
}
