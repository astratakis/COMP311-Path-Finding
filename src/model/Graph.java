package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Graph {

	public Graph(Scanner scanner) throws IllegalArgumentException {
		String line = scanner.nextLine();
		
		nodes = new HashMap<String, Node>();
		roads = new HashMap<String, Road>();
		
		while (!line.contentEquals("</Roads>")) {
			
			String[] arr = line.split("; ");
			
			if (!nodes.containsKey(arr[1])) {
				nodes.put(arr[1], new Node(arr[1]));
			}
			if (!nodes.containsKey(arr[2])) {
				nodes.put(arr[2], new Node(arr[2]));
			}
			
			roads.put(arr[0], new Road(arr[0], nodes.get(arr[1]), nodes.get(arr[2]), Integer.parseInt(arr[3])));
			line = scanner.nextLine();
		}
		
		scanner.nextLine();
		scanner.nextLine();
	}
	
	Map<String, Road> roads;
	Map<String, Node> nodes;
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (Road r : roads.values()) {
			buffer.append(r + "\n");
		}
		return buffer.toString();
	}
}
