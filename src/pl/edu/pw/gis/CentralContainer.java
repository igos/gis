package pl.edu.pw.gis;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	private HashSet<String> result;

	public CentralContainer() {
		this.map = new HashMap<String, TreeSet<String>>();
		this.result = new HashSet<String>();
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
		for (String k : result) {
			 sb.append(" -> "+ k + "\n");
		 }
		return sb.toString();
	}

	/**
	 * Method looks at centrals and vertices and tries to optimize this so each
	 * vertex appears in one list.
	 */
	public void optimize() {
		while(!map.isEmpty()) {
			//select the biggest
			String key = null;
			int size = 0;
			for (String k : map.keySet()) {
				if(map.get(k) == null && size == 0) {
					key = k;
				}
				if(size <= map.get(k).size()) {
					key = k;
					size = map.get(k).size();
				}
			}
			//it's our new central
			result.add(key);
			//remove from graph
			if(map.get(key) != null) {
				for (String val : map.get(key)) {
					map.values().removeAll(Collections.singleton(val));
					map.remove(val);
				}
			}
			map.remove(key);
		}
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
		return result;
	}

	public boolean isCentral(String v) {
		return result.contains(v);
	}
}