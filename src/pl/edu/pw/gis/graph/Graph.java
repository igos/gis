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
public class Graph<String,DefaultWeightedEdge> extends ListenableUndirectedWeightedGraph<String,DefaultWeightedEdge>  {
	public Graph(Class<? extends DefaultWeightedEdge> arg0) {
		super(arg0);
	}

	public JGraphXAdapter<String,DefaultWeightedEdge> getXGraph() {
		return new JGraphXAdapter<String,DefaultWeightedEdge>(this);
	
	}
	///Adds edge with weight
	public DefaultWeightedEdge addWeightedEdge(String e1, String e2, Double weight) {
		DefaultWeightedEdge temp = this.addEdge(e1, e2);
		this.setEdgeWeight(temp, weight);
		return temp;
	}

}
