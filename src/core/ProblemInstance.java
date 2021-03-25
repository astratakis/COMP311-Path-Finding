package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import model.*;

public class ProblemInstance {
	
	private Node source;
	private Node destination;
	private Graph graph;
	
	private Vector<Day> days;
	
	private static final Node NO_PRED = null;
	private static final double FOUND = -1.0;
	private int visitedNodesStar = 0;

	
	public ProblemInstance() {
		graph = new Graph();
		days = new Vector<Day>();		
	}
	
	public void simulate() {
		System.out.println("\n------------------------- Simulation and evaluation results ------------------------\n");
		findCheapestNeighbors(days.get(0));
		idastar(days.get(0));
		
		
		/*
		for(Day d: days) {
			findCheapestNeighbors(d);
			
			
			System.out.println("Day: " + (d.getID()+1));
			
			bfs(graph,source,destination,d);
			dijkstra(d);

			for(Result r : d.getResults()) {
				r.printResult();
			}
			
			System.out.println("--------------------------------------");
			
		}		*/
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
			
			Road r = Collections.min(graph.getRoadNodes().get(setToSearch), new Comparator<Road>() {

				@Override
				public int compare(Road r1, Road r2) {
					if (d.getPredictions().get(r1) > d.getPredictions().get(r2)) {
						return 1;
					}
					else if (d.getPredictions().get(r1) == d.getPredictions().get(r2)) {
						return 0;
					}
					return -1;
				}
				
			});
						
			roadPath.add(r);			
		}
		return roadPath;
	}
	
	
	private void findCheapestNeighbors(Day d) {
		
		for(Node n:  graph.getNodes().values()) {
			for(Road r : n.getNeighbors().keySet()) {
				if(!n.getCheaperCosts().containsKey(n.getNeighbors().get(r))) {
					n.getCheaperCosts().put(n.getNeighbors().get(r), r);
				}else {
					Road existing = n.getCheaperCosts().get(n.getNeighbors().get(r));
					
					if(d.getPredictions().get(existing) > d.getPredictions().get(r)){
						n.getCheaperCosts().put(n.getNeighbors().get(r),r);
					}
				}
			}
		}
	}
	
	
	private LinkedList<Node> bfs(Graph graph, Node source, Node destination, Day d) {
		
		//Results stuff
		Result r1 = new Result(d);
		long start = System.nanoTime();
		int visitedNodes = 0;

		LinkedList<Node> queue = new LinkedList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		LinkedList<Node> spt = new LinkedList<Node>();
		
		
		path.add(source);
		queue.add(source);
		explored.add(source);
		
		Node curNode;

		while(!queue.isEmpty()) {
			curNode = queue.pop();
			for(Node n: curNode.getNeighbors().values()) {
				
				if(!explored.contains(n)){
					queue.add(n);
					explored.add(n);
					path.add(n);
					
					if(curNode.equals(destination)) {	
						break;
					}
				}
				
				visitedNodes++;
			}
		}
		
		Node currentSrc = destination;
		spt.add(destination);
		
		while(!path.isEmpty()) {
			curNode = path.get(path.size()-1);
			path.remove(curNode);
			
			if(currentSrc.getNeighbors().containsValue(curNode)) {
				spt.add(curNode);
				currentSrc = curNode;
				
				if(curNode.equals(source)) {
					break;
				}
			}			
		}
		
		LinkedList<Road> roadPath = findRoadPath(spt, d);
		double totCost = 0.0;
		double realCost = 0.0;
		
		long end = System.nanoTime();
		
		Collections.reverse(roadPath);
		
		for(Road r: roadPath) {
			totCost += d.getPredictions().get(r);
			realCost += d.getActual().get(r);
		}
		
		r1.setActualCost(realCost);
		r1.setPredictedCost(totCost);
		r1.setAlgorithmName("BREADTH FIRST SEARCH");
		r1.setExecutionTime(end-start);
		r1.setVisitedNodes(visitedNodes);
		r1.setRoadPath(roadPath);
		
		d.getResults().add(r1);
						
		return spt;
	}
	
	
	public void dijkstra(Day d) {
		source.predecessor = NO_PRED;
		source.smallestDistance = 0;
		
		HashSet<Node> visited = new HashSet<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		Result r2 = new Result(d);
		
		long start = System.nanoTime();
		int visitedNodes = 0;
		
		Node curNode = source;
		while(true) {
			visitedNodes++;

			for(Node n: curNode.getAdjacencies()) {
				double distance = d.getPredictions().get(curNode.getCheaperCosts().get(n));
				
				if(curNode.predecessor != null) {
					distance+=curNode.smallestDistance;
				}
				
				if(distance< n.smallestDistance) {
					n.predecessor = curNode;
					n.smallestDistance = distance;
				}
				visitedNodes++;
			}

			visited.add(curNode);				
			List<Node> toCheck = new ArrayList<Node>(graph.getNodes().values());
			toCheck.removeAll(visited);
			try {
				curNode = Collections.min(toCheck, new Comparator<Node>() {
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
			}catch(Exception e) {
				break;
			}
		}
		
		curNode = destination;
		
		long end = System.nanoTime();
		
		
		while(!curNode.equals(source)) {
			path.add(curNode);
			curNode = curNode.predecessor;
		}
		path.add(source);

		double realCost = 0.0;

		LinkedList<Road> roadPath = new LinkedList<Road>();
		Collections.reverse(path);
		roadPath = findRoadPath(path,d);
		
		for(Road r: roadPath) {
			realCost += d.getActual().get(r);
		}
		
		r2.setAlgorithmName("DIJKSTRA");
		r2.setExecutionTime(end-start);
		r2.setPredictedCost(graph.getNodes().get(destination.getName()).smallestDistance);
		r2.setActualCost(realCost);
		r2.setRoadPath(roadPath);
		r2.setVisitedNodes(visitedNodes);
		d.getResults().add(r2);
		
		for(Node n : graph.getNodes().values()) {
			n.predecessor = NO_PRED;
			n.smallestDistance = Double.POSITIVE_INFINITY;
		}
	}
	
	public void idastar(Day d) {
		for(Node n : graph.getNodes().values()) {
			initializeHeuristics(n);
		}
		
		double bound = heuristic(source, destination);
		
		LinkedList<Node> path = new LinkedList<Node>();
		path.add(source);

		while (true) {
			
			double type = search(path, 0, bound);
			
			if (type == Double.POSITIVE_INFINITY) {
				System.out.println("Not found ...");
			}
			else if (type == FOUND) {
				System.out.println("Found");
				path.push(source);
				System.out.println(this.visitedNodesStar);
				System.out.println(path);
				break;
			}
			bound = type;
		}
		
	}
	
	private double search(LinkedList<Node> path, double cost, double bound) {
		Node curNode = path.getLast();
		visitedNodesStar++;

		double f = cost + heuristic(curNode, destination);
		
		if (f > bound) {
			return f;
		}
		
		if (curNode.equals(destination)) {
			return FOUND;
		}
		
		double min = Double.POSITIVE_INFINITY;
		
		for (Node n : curNode.getAdjacencies()) {
			if (!path.contains(n)) {
				visitedNodesStar++;
				path.push(n);
				double type = search(path, cost + curNode.getCheaperCosts().get(n).getWeight(), bound);
				
				if (type == FOUND) {
					return FOUND;
				}
				else if (type < min) {
					min = type;
				}
				path.pop();
			}
		}
		
		return min;
	}
	
	public double heuristic(Node sourceN, Node destN) {
		return sourceN.heuristics.get(destN);
	}
	
	public void initializeHeuristics(Node sourceNode) {
		sourceNode.predecessor = NO_PRED;
		sourceNode.smallestDistance = 0;
		
		HashSet<Node> visited = new HashSet<Node>();

		Node curNode = sourceNode;
		while(true) {

			for(Node n: curNode.getAdjacencies()) {
				double distance = curNode.getCheaperCosts().get(n).getWeight();
				
				if(curNode.predecessor != null) {
					distance+=curNode.smallestDistance;
				}
				
				if(distance< n.smallestDistance) {
					n.predecessor = curNode;
					n.smallestDistance = distance;
				}
			}

			visited.add(curNode);				
			List<Node> toCheck = new ArrayList<Node>(graph.getNodes().values());
			toCheck.removeAll(visited);
			try {
				curNode = Collections.min(toCheck, new Comparator<Node>() {
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
			}catch(Exception e) {
				break;
			}
		}
		
		
		for(Node n: graph.getNodes().values()) {
			sourceNode.heuristics.put(n, n.smallestDistance);
		}

		for(Node n : graph.getNodes().values()) {
			n.predecessor = NO_PRED;
			n.smallestDistance = Double.POSITIVE_INFINITY;
		}		
		
		
	}
	

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	

	public Vector<Day> getDays() {
		return days;
	}


	public void printProblemInstance() {
		System.out.println("--------------------- The Problem Instance Info -----------------------");
		System.out.println("\nFrom: "+source.getName() +" ----> To: "+destination.getName());
		System.out.println("\n-----> Nodes");

		for(Node n: graph.getNodes().values() ) {
			System.out.println(n.getName());
			System.out.println("     ***Neighbor nodes***");
			for(Node e: n.getAdjacencies()) {
				System.out.println("\t" +e.getName());
			}
			
			System.out.println("     ***Neighbor roads***");
			for(Road r: n.getRoads()) {
				System.out.println("\t" +r.getName());
			}
			
			System.out.println("    ***Connection info***");
			for(Road s: n.getNeighbors().keySet()) {
				System.out.println("\t" +s.getName()+" to "+n.getNeighbors().get(s).getName());
			}
			System.out.println("------------------------");
		}
		
		System.out.println("\n-----> Roads");
		
		for(Road r: graph.getRoads().values() ) {
			System.out.println("\t"+r.getName() + " --> Normal weight:   " +r.getWeight() + " ||   day 0 pred weight= " + days.get(0).getPredictions().get(r) + "     actual weight= "+ days.get(0).getActual().get(r) );
		}
	}

}
