package model;

import java.util.Map;

public class Graph {
	
	public Graph(Map<String, Node> nodes, Map<String, Road> roads) {
		this.nodes = nodes;
		this.roads = roads;
	}
	
	final Map<String, Node> nodes;
	final Map<String, Road> roads;
}
