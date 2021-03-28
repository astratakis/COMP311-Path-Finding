package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author astratakis
 * @author dpetrou
 * 
 * It represents point or a city in a map.
 * It is the same as a vertex on an abstract graph.
 * Each Node is described by its name, its neighbors and its heuristics.
 */
public class Node {

	/**
	 * Created a new node with no neighbors and no heuristics.
	 * @param name The name of the node.
	 */
	public Node(String name) {
		this.name = name;
		
		// set smallest distance to predecessor = infinity
		smallestDistance = Double.POSITIVE_INFINITY;
		
		neighbors = new HashMap<Road, Node>();
		adjacencies = new ArrayList<Node>();
		roads = new ArrayList<Road>();
	}
	
	/**
	 * The name of the node.
	 */
	final String name;
	
	/**
	 * The neighbors of this node. Its a table of Nodes indexed by the roads that connect them.
	 */
	Map<Road, Node> neighbors;
	
	/**
	 * All the neighbor nodes. It is the same as all the values of the @see neighbors.
	 */
	List<Node> adjacencies;
	
	/**
	 * A set of the cheapest roads that lead to neighbors indexed by the neighbor nodes.
	 * This is used because a pair of Nodes can be connected by multiple Roads thus only
	 * the roads with the smallest weights are used by the path finding algorithms.
	 */
	Map<Node, Road> cheapestRoads;
	
	List<Road> roads;
	
	/**
	 * The nearest predecessor of the node.
	 * It is used by the Dijkstra algorithm.
	 */
	Node predecessor;
	
	/**
	 * The distance to the nearest predecessor.
	 * It is used by the Dijkstra algorithm.
	 */
	double smallestDistance;
	
	@Override
	public String toString() {
		return name;
	}
}
