package pl.edu.pw.gis;

import javax.swing.JFrame;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import pl.edu.pw.gis.graph.JGraphXAdapter;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        Graph<String, DefaultEdge> g = new Graph<String, DefaultEdge>(DefaultEdge.class);

        // add some sample data (graph manipulated via JGraphT)
        g.addVertex( "v1" );
        g.addVertex( "v2" );
        g.addVertex( "v3" );
        g.addVertex( "v4" );

        g.addEdge( "v1", "v2" );
        g.addEdge( "v2", "v3" );
        g.addEdge( "v3", "v1" );
        g.addEdge( "v4", "v3" );

        JGraphXAdapter<String, DefaultEdge> graph = new JGraphXAdapter<String, DefaultEdge>(g);

        JFrame frame = new JFrame();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);

        g.addVertex( "v5" );
        g.addVertex( "v6" );
        g.addVertex( "v7" );
        g.addVertex( "v8" );

        graph.getModel().beginUpdate();
        double x = 20, y = 20;
        for (mxCell cell : graph.getVertexToCellMap().values()) {
            graph.getModel().setGeometry(cell, new mxGeometry(x, y, 20, 20));
            x += 40;
            if (x > 200) {
                x = 20;
                y += 40;
            }
        }
        graph.getModel().endUpdate();
	}

}
