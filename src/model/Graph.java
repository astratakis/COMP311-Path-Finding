package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class Graph {

	@SuppressWarnings("unchecked")
	public Graph(Scanner scanner) throws IllegalArgumentException {
		String line = scanner.nextLine();
		
		nodes = new HashMap<String, Node>();
		roads = new HashMap<String, Road>();
		roadNodes = new HashMap<HashSet<Node>, ArrayList<Road>>();
		HashSet<Node> nodesHashSet = new HashSet<Node>();
		
		while (!line.contentEquals("</Roads>")) {
			
			String[] arr = line.split("; ");
			
			if (!nodes.containsKey(arr[1])) {
				nodes.put(arr[1], new Node(arr[1]));
			}
			if (!nodes.containsKey(arr[2])) {
				nodes.put(arr[2], new Node(arr[2]));
			}
			
			Road r = new Road(arr[0], nodes.get(arr[1]), nodes.get(arr[2]), Integer.parseInt(arr[3]));
			
			roads.put(arr[0], r);
			
			Node start = nodes.get(arr[1]);
			Node end = nodes.get(arr[2]);
			
			nodesHashSet.add(start);
			nodesHashSet.add(end);
			
			System.out.println(nodesHashSet);
			
			if (!roadNodes.containsKey(nodesHashSet)) {
				ArrayList<Road> list = new ArrayList<Road>();
				list.add(r);
				roadNodes.put(nodesHashSet, list);
			}
			else {
				roadNodes.get(nodesHashSet).add(r);
			}
								
			start.neighbors.put(roads.get(arr[0]),end);
			end.neighbors.put(roads.get(arr[0]),start);
			
			nodesHashSet.clear();
			line = scanner.nextLine();
		}
		
		for (HashSet<Node> hs : roadNodes.keySet()) {
			System.out.println(hs);
			System.out.println("Road: " + roadNodes.get(hs));
			System.out.println();
		}
		
		scanner.nextLine();
	}
	
	Map<String, Road> roads;
	Map<String, Node> nodes;
	Map<HashSet<Node>, ArrayList<Road>> roadNodes;
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (Road r : roads.values()) {
			buffer.append(r + "\n");
		}
		return buffer.toString();
	}
}
