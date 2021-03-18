package model;

import java.util.HashMap;
import java.util.Map;

public class Node {
	
	public Node(String name) {
		this.name = name;
		this.neighbors = new HashMap<Road,Node>();
	}
	
	final String name;
	Map<Road,Node> neighbors;
	
	@Override
	public String toString() {
		return name;
	}
	
	public void printNeighbors() {
		System.out.println("------- Node : "+this.name+"-------");
		for(Road r : neighbors.keySet()) {
			System.out.println(r.name+ " connecting with "+neighbors.get(r));
		}
		System.out.println();
	}
}
