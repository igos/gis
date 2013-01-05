package pl.edu.pw.gis.convert;

import pl.edu.pw.gis.GraphConvertStrategy;
import pl.edu.pw.gis.graph.Graph;

public class Filter implements GraphConvertStrategy {
	private int r;
	
	public Filter(int r) {
		this.r = r;
	}
	
	@Override
	public Graph convert(Graph graph) {
		

		Set<DefaultEdge> edges = graph.getTGraph().edgeSet();

		for (DefaultEdge e : edges) {
			gv.addln(String.format("\"%s\" -> \"%s\"", g.getEdgeSource(e), g.getEdgeTarget(e)));			
		}
	}

}
