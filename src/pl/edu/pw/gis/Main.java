package pl.edu.pw.gis;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import pl.edu.pw.gis.convert.Filter;
import pl.edu.pw.gis.graph.Graph;
import pl.edu.pw.gis.graph.JGraphXAdapter;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
        Graph<String, DefaultWeightedEdge> g = new Graph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        // add some sample data
        g.addVertex( "v1" );
        g.addVertex( "v2" );
        g.addVertex( "v3" );
        g.addVertex( "v4" );

        g.addWeightedEdge( "v1", "v2", 10.0 );
        g.addWeightedEdge( "v2", "v3", 20.0 );
        g.addWeightedEdge( "v3", "v1", 12.0 );
        g.addWeightedEdge( "v4", "v3", 9.0 );
        
        
        // use ConvertStrategy
        Filter filter = new Filter(30.0);
        g = filter.convert(g);
        
        //display
        JGraphXAdapter<String, DefaultWeightedEdge> graph = g.getXGraph();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        
        
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(BorderLayout.CENTER, graphComponent);
        frame.setVisible(true);
        
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
	    try {   
	    	
	    } finally {
	         graph.getModel().endUpdate();
	    }
        
	 // define layout
        mxIGraphLayout layout = new mxFastOrganicLayout(graph);

        // layout using morphing
        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);

            morph.addListener(mxEvent.DONE, new mxIEventListener() {

                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                    graph.getModel().endUpdate();
                    // fitViewport();
                }

            });

            morph.startAnimation();
        }
//        //dummy autogeometry
//        graph.getModel().beginUpdate();
////        double x = 20, y = 20;
////        for (mxCell cell : graph.getVertexToCellMap().values()) {
////            graph.getModel().setGeometry(cell, new mxGeometry(x, y, 20, 20));
////            x += 40;
////            y += 40;
////            if (x > 200) {
////                x = 20;
////                y += 40;
////            }
////        }
//        graph.getModel().endUpdate();
	}

}
