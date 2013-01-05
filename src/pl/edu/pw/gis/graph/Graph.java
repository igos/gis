package pl.edu.pw.gis.graph;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.mxgraph.*;
import com.mxgraph.view.mxGraph;

public class Graph<String,DefaultEdge> {
	ListenableUndirectedWeightedGraph<String,DefaultEdge> r;
	public Graph() {
		
	}
	
	
	public JGraphXAdapter<String,DefaultEdge> getXGraph() {
		return new JGraphXAdapter<String,DefaultEdge>(r);
		
	}
	public ListenableUndirectedWeightedGraph<String,DefaultEdge> getTGraph() {
		return r;
	}
}
