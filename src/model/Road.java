package model;

import java.util.Vector;

public class Road {
	
	private String name;
	private Node start;
	private Node end;
	
	private double weight;
	
	
	public Road() {
		
	}
	
	public Road(String name, Node start, Node end, double w) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.weight = w;
	}
	
	public Vector<Node> getNodes(){
		Vector<Node> n = new Vector<Node>();
		return n;
	}	
	
	public String getName() {
		return name;
	}

	public Node getStart() {
		return start;
	}
	
	public Node getEnd() {
		return end;
	}
	
	public double getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
