package pl.edu.pw.gis.convert;

import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import pl.edu.pw.gis.GraphConvertStrategy;
import pl.edu.pw.gis.graph.Graph;

public class Filter implements GraphConvertStrategy {
	private int r;
	
	public Filter(int r) {
		this.r = r;
	}
	
	@Override
	public Graph convert(Graph graph) {
		Graph temp;
		
		Set<DefaultEdge> edges = graph.getTGraph().edgeSet();
		
		

		for (DefaultEdge e : edges) {
			
			
			gv.addln(String.format("\"%s\" -> \"%s\"", graph.getTGraph().getEdgeSource(e), graph.getTGraph().getEdgeTarget(e)));			
		}
	}

}
