package pl.edu.pw.gis.graph;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.mxgraph.*;
import com.mxgraph.view.mxGraph;

public class Graph<String,DefaultEdge> extends ListenableUndirectedWeightedGraph<String,DefaultEdge>  {
	
	public Graph(WeightedGraph<String, DefaultEdge> arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public JGraphXAdapter<String,DefaultEdge> getXGraph() {
		return new JGraphXAdapter<String,DefaultEdge>(this);
		
	}
}
