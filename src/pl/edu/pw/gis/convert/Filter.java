package pl.edu.pw.gis.convert;

import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import pl.edu.pw.gis.GraphConvertStrategy;
import pl.edu.pw.gis.graph.Graph;

public class Filter implements GraphConvertStrategy {
	private double r;
	
	public Filter(double r) {
		this.r = r;
	}
	
	@Override
	public Graph convert(Graph graph) {
		Graph temp = graph.clone();
		
		Set<DefaultEdge> edges = graph.edgeSet();
		
		for (DefaultEdge e : edges) {
			if(graph.getEdgeWeight(e) > r) {
				temp.removeEdge(e);
			}
			
			gv.addln(String.format("\"%s\" -> \"%s\"", graph.getEdgeSource(e), graph.getEdgeTarget(e)));			
		}
	}

}
