package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Graph {
	
	
	Map<String, Road> roads;
	Map<String, Node> nodes;
	Map<HashSet<Node>, ArrayList<Road>> roadNodes;

	
	public Graph() {
		roads = new HashMap<String,Road>();
		nodes = new HashMap<String,Node>();
		roadNodes = new HashMap<HashSet<Node>, ArrayList<Road>>();
	}

	public Map<String, Road> getRoads() {
		return roads;
	}

	public Map<String, Node> getNodes() {
		return nodes;
	}

	public Map<HashSet<Node>, ArrayList<Road>> getRoadNodes() {
		return roadNodes;
	}
	
	

}
