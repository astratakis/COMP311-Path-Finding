package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author astratakis
 * @author dpetrou
 * 
 * Implementation of the Real Time Learning A* algorithm.
 */
public class LearingRealTimeAstar implements PathFinder {
	
	/**
	 * Initialize the algorithm.
	 * @param graph The test graph.
	 */
	public LearingRealTimeAstar(Graph graph) {
		this.graph = graph;
		heuristics = new HashMap<Node, Double>();
		
		// Initiate the heuristics to 0 since the algorithm does not know the distance of each node to the destination.
		for (Node n : this.graph.nodes.values()) {
			heuristics.put(n, 0.0);
		}
	}
	
	/**
	 * The graph that will be searched.
	 */
	private final Graph graph;
	
	/**
	 * The heuristic values for each node to the destination. Initially 0
	 */
	private Map<Node, Double> heuristics;

	@Override
	public Result execute(Node source, Node destination, Day day) {
		
		Node current = source;
		double cost = 0.0;
		Node nextNode = null;
		Road nextRoad = null;
		double minimum;
		
		LinkedList<Road> path = new LinkedList<Road>();
		ArrayList<Double> weights = new ArrayList<Double>();
		//LinkedList<Node> nodePath = new LinkedList<Node>();
		int visitedNodes = 1;
		
		long start = System.nanoTime();
		
		while (true) {
			minimum = Double.MAX_VALUE;
			
			if (nextRoad != null) {
				path.add(nextRoad);
			}
			
			if (current.equals(destination)) {
				// Reached destination...
				break;
			}
			
			// Iterate through all the neighbor roads of the current node and find the cheapest.
			for (Road r : current.neighbors.keySet()) {
				
				Node prime = null;
				
				if (current.equals(r.start)) {
					// if the current node is the starting node of this road then
					// get the ending node of this road r
					
					prime = r.end;
				}
				else {
					// if the current node is the ending node of this road then
					// get the starting node of this road r
					
					prime = r.start;
				}
				
				// Find the node that is "closest" to the finish.
				if (heuristics.get(prime) < minimum) {
					minimum = heuristics.get(prime);
					nextNode = prime;
					nextRoad = r;
				}
			}
			
			// Update the heuristics table with the actual weight that the algorithm discovered.
			// At last make this move.
			heuristics.put(current, heuristics.get(nextNode) + day.actual.get(nextRoad));
			current = nextNode;
			cost += day.actual.get(nextRoad);
			weights.add(day.actual.get(nextRoad));
			visitedNodes++;
		}
		
		long end = System.nanoTime();
		
		// Create the result and return it;
		Result result = new Result("LRTA*", visitedNodes, end - start, path, weights, cost, cost);
		return result;
	}
}
