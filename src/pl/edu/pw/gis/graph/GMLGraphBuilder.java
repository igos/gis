package pl.edu.pw.gis.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLReader;

public class GMLGraphBuilder {

	private Graph<String, DefaultWeightedEdge> wrapped;
	private TinkerGraph graph;

	public GMLGraphBuilder(Graph<String, DefaultWeightedEdge> wrapped) {
		this.wrapped = wrapped;
		graph = new TinkerGraph();
	}

	public void readGMLFile(String filepath) throws IOException {
		GMLReader gmlReader = new GMLReader(graph);
		if (!new File(filepath).canRead()) {
			throw new FileNotFoundException("file '" + filepath
					+ "' cannot be opened for read");
		}
		gmlReader.inputGraph(new FileInputStream(filepath));
		for (Vertex v : graph.getVertices()) {
			String id = (String) v.getId();
			System.out.println("loaded vertex: " + id);
			wrapped.addVertex(id);
		}

		for (Edge e : graph.getEdges()) {
			String id = (String) e.getId();
			Number weight = (Number) e.getProperty("weight");
			if (weight == null) {
				throw new IOException(
						"Invalid GML file format, each edge should has 'weight' property");
			}
			System.out.println("loaded edge: " + id + ", from "
					+ e.getVertex(Direction.IN).getId() + " to "
					+ e.getVertex(Direction.OUT).getId() + " with weight "
					+ weight);

			// add edge, read edge's value first
			wrapped.addWeightedEdge(e.getVertex(Direction.IN).getId()
					.toString(), e.getVertex(Direction.OUT).getId().toString(),
					weight.doubleValue());
		}
	}
}
