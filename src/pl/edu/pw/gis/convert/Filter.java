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
		Graph temp = new Graph<String, DefaultEdge>(DefaultEdge.class);
		
		Set<DefaultEdge> edges = graph.getTGraph().edgeSet();
		
		for (DefaultEdge e : edges) {
			if(graph.getTGraph().getEdgeWeight(e) <= r) {
				temp.
			}
			
			gv.addln(String.format("\"%s\" -> \"%s\"", graph.getTGraph().getEdgeSource(e), graph.getTGraph().getEdgeTarget(e)));			
		}
	}

}
