package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * @author astratakis
 * @author dpetrou
 * 
 * It describes an abstract graph.
 */
public class Graph {
	
	/**
	 * Creates a graph with predefined nodes and roads.
	 * @param nodes The nodes of the graph.
	 * @param roads The roads of the graph.
	 * @param roadNodes A set of nodes that corresponds to roads
	 */
	public Graph(Map<String, Node> nodes, Map<String, Road> roads, Map<HashSet<Node>, ArrayList<Road>> roadNodes) {
		this.nodes = nodes;
		this.roads = roads;
		this.roadNodes = roadNodes;
	}
	
	/**
	 * The nodes of the graph.
	 */
	final Map<String, Node> nodes;
	
	/**
	 * The roads of the graph.
	 */
	final Map<String, Road> roads;
	
	/**
	 * A set of nodes that corresponds to roads.
	 */
	final Map<HashSet<Node>, ArrayList<Road>> roadNodes;
	
	/**
	 * Resets dijkstra's values from all the nodes of the graph.
	 */
	void resetNodes() {
		for (Node n : nodes.values()) {
			n.predecessor = null;
			n.smallestDistance = Double.POSITIVE_INFINITY;
		}
	}
}
