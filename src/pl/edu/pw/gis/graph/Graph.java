package pl.edu.pw.gis.graph;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import com.mxgraph.*;
import com.mxgraph.view.mxGraph;

/**
 * Wrapper na JGraphT.Graph z mozliwoscia konwersji na JGraphX
 * 
 * @author Igor
 *
 * @param <String>
 * @param <DefaultEdge>
 */
public class Graph<String,DefaultEdge> extends ListenableUndirectedWeightedGraph<String,DefaultEdge>  {
	public Graph(Class<? extends DefaultEdge> arg0) {
		super(arg0);
	}

	public JGraphXAdapter<String,DefaultEdge> getXGraph() {
		return new JGraphXAdapter<String,DefaultEdge>(this);
	
	}
	
	public void addWeightedEdge(String e1, String e2, Double weight) {
		this.addEdge(arg0, arg1)
	}

}
