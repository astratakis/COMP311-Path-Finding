package model;

import java.util.HashMap;
import java.util.Vector;

public class Node {
	
	private String name;
	private HashMap<Road,Node> neighbors;
	private Vector<Node> adjacencies;
	private Vector<Road> roads;
	private HashMap<Node,Road> cheaperCosts;
	public HashMap<Node,Double> heuristics;
	public Node predecessor;
	public double smallestDistance;

	public double scaledWeight = 0.0;

		
	public Node() {
		
	}
	
	public Node(String n) {
		this.name = n;
		neighbors = new HashMap<Road,Node>();
		adjacencies = new Vector<Node>();
		roads = new Vector<Road>();
		cheaperCosts = new HashMap<Node,Road>();
		heuristics = new HashMap<Node,Double>();
		predecessor = null;
		smallestDistance  = Double.POSITIVE_INFINITY;
	}
	
	public String getName() {
		return name;
	}

	public Vector<Node> getAdjacencies() {
		return adjacencies;
	}

	public Vector<Road> getRoads() {
		return roads;
	}	

	public HashMap<Road,Node> getNeighbors() {
		return neighbors;
	}

	public HashMap<Node, Road> getCheaperCosts() {
		return cheaperCosts;
	}

	public void setCheaperCosts(HashMap<Node, Road> cheaperCosts) {
		this.cheaperCosts = cheaperCosts;
	}
	
	@Override
	public String toString() {
		return name;
	}

	
}
