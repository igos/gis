package pl.edu.pw.gis.graph;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.mxgraph.*;
import com.mxgraph.view.mxGraph;

public class Graph<String,DefaultEdge> extends ListenableUndirectedWeightedGraph<String,DefaultEdge>  {
	public Graph(Class<? extends DefaultEdge> arg0) {
		super(arg0);
	}

	public JGraphXAdapter<String,DefaultEdge> getXGraph() {
		return new JGraphXAdapter<String,DefaultEdge>(this);
		
	}
}
