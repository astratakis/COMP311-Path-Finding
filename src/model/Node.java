package model;

public class Node {
	
	public Node(String name) {
		this.name = name;
	}
	
	final String name;
	
	@Override
	public String toString() {
		return name;
	}
}
