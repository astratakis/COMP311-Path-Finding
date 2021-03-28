package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Graph {
	
	public Graph(Map<String, Node> nodes, Map<String, Road> roads, Map<HashSet<Node>, ArrayList<Road>> roadNodes) {
		this.nodes = nodes;
		this.roads = roads;
		this.roadNodes = roadNodes;
	}
	
	final Map<String, Node> nodes;
	final Map<String, Road> roads;
	final Map<HashSet<Node>, ArrayList<Road>> roadNodes;
	
	void resetNodes() {
		for (Node n : nodes.values()) {
			n.predecessor = null;
			n.smallestDistance = Double.POSITIVE_INFINITY;
		}
	}
}
