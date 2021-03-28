package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IterativeDeepeningAstar implements PathFinder {

	private final static double FOUND = Double.NEGATIVE_INFINITY;
	
	public IterativeDeepeningAstar(Graph graph, Node destination) {
		this.graph = graph;
		
		heuristics = new HashMap<Node, Double>();
		this.graph.resetNodes();
		visitedNodes = 0;
		
		// initialize heuristics
		findCheapestNeighbors();
		dijkstra(destination);
		
		for (Node n : graph.nodes.values()) {
			heuristics.put(n, n.smallestDistance);
		}
	}
	
	private final Graph graph;
	
	private int visitedNodes;
	private LinkedList<Node> nodePath;
	private ArrayList<Double> weights;
	private LinkedList<Road> roadPath;
	
	private Map<Node, Double> heuristics;
	
	@Override
	public Result execute(Node source, Node destination, Day day) {
		
		// perform dijkstra at the beggining to initialize heuristics..
		
		double bound = heuristics.get(source);
		nodePath = new LinkedList<Node>();
		weights = new ArrayList<Double>();
		
		long start = System.nanoTime();
		
		nodePath.add(source);
		
		while (true) {
			
			double type = search(0, bound, destination);
			
			if (type == Double.POSITIVE_INFINITY) {
				System.err.println("Not found");
				System.exit(-1);
			}
			else if (type == FOUND) {
				break;
			}
			bound = type;
		}
		
		long end = System.nanoTime();
		
		double actualCost = 0.0;
		double predictedCost = 0.0;
		
		roadPath = findRoadPath(nodePath, day);
		
		for (Road r : roadPath) {
			weights.add(day.predictions.get(r));
			predictedCost += day.predictions.get(r);
			actualCost += day.actual.get(r);
		}
		
		Result result = new Result("IDA*", visitedNodes, end - start, roadPath, weights, predictedCost, actualCost);
		return result;
	}
	
	private LinkedList<Road> findRoadPath(LinkedList<Node> nodePath, Day d){
		
		LinkedList<Road> roadPath = new LinkedList<Road>();
		
		for(int i=0; i<nodePath.size(); i++) {
			
			if(nodePath.isEmpty() || i+1 >= nodePath.size()) {
				break;
			}
			
			Node n1 = nodePath.get(i);
			Node n2 = nodePath.get(i+1);
			
			Node[] nodeArray = {n1,n2};
			HashSet<Node> setToSearch = new HashSet<Node>(Arrays.asList(nodeArray));
			
			Road r = Collections.min(graph.roadNodes.get(setToSearch), new Comparator<Road>() {

				@Override
				public int compare(Road r1, Road r2) {
					if (d.predictions.get(r1) > d.predictions.get(r2)) {
						return 1;
					}
					else if (d.predictions.get(r1) == d.predictions.get(r2)) {
						return 0;
					}
					return -1;
				}
				
			});
						
			roadPath.add(r);			
		}
		return roadPath;
	}
	
	private double search(double cost, double bound, Node destination) {
		Node current = nodePath.getLast();
		
		double f = cost + heuristics.get(current);
		
		if (current.equals(destination)) {
			return FOUND;
		}
		
		if (f > bound) {
			return f;
		}
		
		double min = Double.POSITIVE_INFINITY;
		
		for (Node n : current.adjacencies) {
			if (!nodePath.contains(n)) {
				
				nodePath.add(n);
				double type = search(cost + current.cheapestRoads.get(n).weight, bound, destination);
				
				if (type == FOUND) {
					return FOUND;
				}
				else if (type < min) {
					min = type;
				}
				nodePath.remove(n);
			}
		}
		
		return min;
	}
	
	private void dijkstra(Node source) {
		source.smallestDistance = 0;
		
		Set<Node> visited = new HashSet<Node>();
				
		Node current = source;
				
		while (true) {
			
			for (Node n : current.adjacencies) {
				double distance = current.cheapestRoads.get(n).weight;
				
				if (current.predecessor != null) {
					distance += current.smallestDistance;
				}
				
				if (distance < n.smallestDistance) {
					n.predecessor = current;
					n.smallestDistance = distance;
				}
			}
			
			visited.add(current);
			List<Node> toCheck = new ArrayList<Node>(graph.nodes.values());
			toCheck.removeAll(visited);
			try {
				current = Collections.min(toCheck, new Comparator<Node>() {

					@Override
					public int compare(Node n1, Node n2) {
						if(n1.smallestDistance > n2.smallestDistance) {
							return 1;
						}else if(n1.smallestDistance == n2.smallestDistance) {
							return 0;
						}
						return -1;
					}
					
				});
			}
			catch (Exception e) {
				break;
			}
		}
	}
	
	private void findCheapestNeighbors() {
		
		for(Node n:  graph.nodes.values()) {
			for(Road r : n.neighbors.keySet()) {
				if(!n.cheapestRoads.containsKey(n.neighbors.get(r))) {
					n.cheapestRoads.put(n.neighbors.get(r), r);
				}else {
					Road existing = n.cheapestRoads.get(n.neighbors.get(r));
					
					if(existing.weight > r.weight){
						n.cheapestRoads.put(n.neighbors.get(r),r);
					}
				}
			}
		}
	}
}
