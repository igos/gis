package pl.edu.pw.gis.graph;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.mxgraph.*;
import com.mxgraph.view.mxGraph;

public class Graph<V,E> {
	SimpleWeightedGraph<String, String> r;
	public Graph() {
		//r.addEdge(arg0, arg1)
		
	}
	
	
	public mxGraph getXGraph() {
		return null;
		
	}
	public org.jgrapht.Graph<V,E> getTGraph() {
		return r;
		
	}
}
