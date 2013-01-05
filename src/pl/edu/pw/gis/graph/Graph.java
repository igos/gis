package pl.edu.pw.gis.graph;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.mxgraph.*;
import com.mxgraph.view.mxGraph;

public class Graph<String,DefaultEdge> {
	ListenableUndirectedWeightedGraph<V, E> r;
	public Graph() {
		
	}
	
	
	public JGraphXAdapter<V,E> getXGraph() {
		return new JGraphXAdapter<V,E>(r);
		
	}
	public org.jgrapht.Graph<V,E> getTGraph() {
		return r;
	}
}
