package model;

public class Road {
	
	public Road(String n, Node s, Node e, int w) {
		name = n;
		start = s;
		end = e;
		weight = w;
	}
	
	final String name;
	final Node start;
	final Node end;
	final int weight;
	int virtualWeight;

}
