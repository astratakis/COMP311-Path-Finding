package model;

/**
 * @author astratakis
 * @author dpetrou
 * 
 * It represents the road that connects 2 Nodes.
 * It is the same as an edge that connects 2 vertices in an abstract graph.
 * Each road is described by its weight, or the cost to cross that road.
 */
public class Road {
	
	/**
	 * Creates a new road connected by exactly 2 Nodes.
	 * @param name The name of the road.
	 * @param start The starting vertex of the road.
	 * @param end The ending vertex of the road.
	 * @param weight The cost to cross the road.
	 */
	public Road(String name, Node start, Node end, double weight) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.weight = weight;
	}
	
	/**
	 * The name of the road.
	 */
	final String name;
	/**
	 * The starting Node of the road.
	 */
	final Node start;
	/**
	 * The ending Node of the road.
	 */
	final Node end;
	/**
	 * The cost to cross the road.
	 */
	final double weight;

	@Override
	public String toString() {
		return name;
	}
}
