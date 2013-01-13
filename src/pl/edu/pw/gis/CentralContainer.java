package pl.edu.pw.gis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * This data structure holds vertices and potential centrals. it also minimizes
 * this set by removing redundant centrals
 * 
 * @author profetes
 * 
 */
class CentralContainer {
	private HashMap<String, TreeSet<String>> map;

	public CentralContainer() {
		this.map = new HashMap<String, TreeSet<String>>();
	}

	/**
	 * Just put the data, vertex and some central that is reachable for this
	 * vertex
	 * 
	 * @param vertex
	 * @param reachableCentral
	 */
	public void put(String vertex, String reachableCentral) {
		// it's reflexive. so each vertex will be added 2 times. code below limits that
		if (Integer.parseInt(vertex) - Integer.parseInt(reachableCentral) > 0)
			return;

		TreeSet<String> set = map.get(vertex);
		if (set == null) {
			set = new TreeSet<String>();
			map.put(vertex, set);
		}

		set.add(reachableCentral);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String k : map.keySet()) {
			sb.append(k + " -> " + Arrays.toString(map.get(k).toArray())
					+ ", length of " + map.get(k).size() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Method looks at centrals and vertices and tries to optimize this so each
	 * vertex appears in one list.
	 */
	public void optimize() {
		/**
		 * - sort by set's length and remove smaller groups of if they are
		 * subsets of bigger ones?
		 */

	}

	/**
	 * If some vertex has no reachable vertices within given range - make sure
	 * such vertex appears in the collection
	 * 
	 * @param justVertex
	 */
	public void put(String justVertex) {
		TreeSet<String> set = map.get(justVertex);
		if (set == null) {
			set = new TreeSet<String>();
			map.put(justVertex, set);
		} else {
			// it's okay, this vertex has been added previously
		}
	}

	public Set<String> getCentrals() {
		return map.keySet();
	}

	public boolean isCentral(String v) {
		return map.containsKey(v);
	}
}