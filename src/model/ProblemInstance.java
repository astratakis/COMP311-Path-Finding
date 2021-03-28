package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProblemInstance {

	public ProblemInstance(Scanner scanner) throws IllegalArgumentException {
		Pattern p = Pattern.compile("(<.+>)(.*)(</.+>)");
		Matcher m = p.matcher(scanner.next());
		
		String source, destination;
		
		if (m.matches())
			source = m.group(2);
		else
			throw new IllegalArgumentException("Input file has wrong format...");
				
		m = p.matcher(scanner.next());
		
		if (m.matches())
			destination = m.group(2);
		else
			throw new IllegalArgumentException("Input file has wrong format...");
		
		scanner.next();			// Consume the next String
		scanner.nextLine();		// Consume the next /n char
		
		String line = scanner.nextLine();
		
		Map<String, Node> nodes = new HashMap<String, Node>();
		Map<String, Road> roads = new HashMap<String, Road>();
		Map<HashSet<Node>, ArrayList<Road>> roadNodes = new HashMap<HashSet<Node>, ArrayList<Road>>();
		
		while (!line.contentEquals("</Roads>")) {
			
			HashSet<Node> nodesHashSet = new HashSet<Node>();
			
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
			
			if (!roadNodes.containsKey(nodesHashSet)) {
				roadNodes.put(new HashSet<Node>(nodesHashSet), new ArrayList<Road>());
				roadNodes.get(nodesHashSet).add(r);
			}
			else {
				roadNodes.get(nodesHashSet).add(r);
			}
								
			start.neighbors.put(roads.get(arr[0]), end);
			end.neighbors.put(roads.get(arr[0]), start);
			
			start.adjacencies.add(end);
			end.adjacencies.add(start);
			
			start.roads.add(r);
			end.roads.add(r);
			
			nodesHashSet.clear();
			line = scanner.nextLine();
		}
		
		scanner.nextLine();
		
		graph = new Graph(nodes, roads, roadNodes);
		days = new ArrayList<Day>();
		
		int index = 0;
		while (true) {
			try {
				days.add(initializeDayPredictions(scanner, ++index));
			}
			catch (Exception e) {
				break;
			}
		}
		
		initializeDayActuals(scanner);
		
		this.source = graph.nodes.get(source);
		this.destination = graph.nodes.get(destination);
	}
	
	private Day initializeDayPredictions(Scanner scanner, int ID) {
		String line = scanner.nextLine();	// Consume <Day>

		Day d = new Day(ID);
		
		while (true) {
			
			line = scanner.nextLine();

			if (line.contentEquals("</Day>")) {
				break;
			}

			String[] arr = line.split(";");
			Road r = graph.roads.get(arr[0]);
			
			Double virtualWeight  = 0.0;
			
			switch (arr[1].trim()) {
			
			case "low":
				// -10%
				virtualWeight = r.weight - 0.1 * r.weight;
				break;
			case "normal":
				virtualWeight = 1.0 * r.weight;
				break;
			case "heavy":
				// +25%
				virtualWeight = r.weight + 0.25 * r.weight;
				break;
			default:
				throw new Error("Should not reach this...");
			}
			
			d.predictions.put(r, virtualWeight);
		}
		
		return d;
	}
	
	private void initializeDayActuals(Scanner scanner) {
		
		for (int i=0; i<days.size(); i++) {
			
			String line = scanner.nextLine(); // Consume <Day>
			
			while (true) {
				line = scanner.nextLine();
				
				if (line.contentEquals("</Day>")) {
					break;
				}
				
				String[] arr = line.split(";");
				
				Road r = graph.roads.get(arr[0]);
				
				Double virtualWeight  = 0.0;
				
				switch (arr[1].trim()) {
				
				case "low":
					// -10%
					virtualWeight = r.weight - 0.1 * r.weight;
					break;
				case "normal":
					virtualWeight = 1.0 * r.weight;
					break;
				case "heavy":
					// +25%
					virtualWeight = r.weight + 0.25 * r.weight;
					break;
				default:
					throw new Error("Should not reach this...");
				}
				
				days.get(i).actual.put(r, virtualWeight);
			}
		}
	}
	
	final Node source;
	final Node destination;
	final Graph graph;
	
	private List<Day> days;
	
	public List<Day> getDays() {	
		return days;
	}
	
	private Result dijkstra(Node source, Day day) {
		source.smallestDistance = 0;
		
		Set<Node> visited = new HashSet<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		ArrayList<Double> weights = new ArrayList<Double>();
		
		int visitedNodes = 0;
		
		Node current = source;
		
		long start = System.nanoTime();
		
		while (true) {
			
			for (Node n : current.adjacencies) {
				double distance = day.predictions.get(current.cheapestRoads.get(n));
				
				if (current.predecessor != null) {
					distance += current.smallestDistance;
				}
				
				if (distance < n.smallestDistance) {
					n.predecessor = current;
					n.smallestDistance = distance;
				}
			}
			
			visitedNodes++;
			visited.add(current);
			List<Node> toCheck = new ArrayList<Node>(graph.nodes.values());
			toCheck.removeAll(visited);
			try {
				current = Collections.min(toCheck, new Comparator<Node>() {

					@Override
					public int compare(Node n1, Node n2) {
						if(n1.smallestDistance > n2.smallestDistance) {
							return 1;
						}else if(n1.smallestDistance == n2.smallestDistance) {
							return 0;
						}
						return -1;
					}
					
				});
			}
			catch (Exception e) {
				break;
			}
		}
		
		current = destination;
		
		while (!current.equals(source)) {
			path.add(current);
			current = current.predecessor;
		}
		path.add(source);
		
		double realCost = 0.0;
		
		LinkedList<Road> roadPath = new LinkedList<Road>();
		Collections.reverse(path);
		roadPath = findRoadPath(path, day);
		
		long end = System.nanoTime();
		
		for(Road r: roadPath) {
			realCost += day.actual.get(r);
			weights.add(day.predictions.get(r));
		}
		
		Result result = new Result("DIJKSTRA", visitedNodes, end - start, roadPath, weights, destination.smallestDistance, realCost);
		return result;
	}
	
	private LinkedList<Road> findRoadPath(LinkedList<Node> nodePath, Day d){
		
		LinkedList<Road> roadPath = new LinkedList<Road>();
		
		for(int i=0; i<nodePath.size(); i++) {
			
			if(nodePath.isEmpty() || i+1 >= nodePath.size()) {
				break;
			}
			
			Node n1 = nodePath.get(i);
			Node n2 = nodePath.get(i+1);
			
			Node[] nodeArray = {n1,n2};
			HashSet<Node> setToSearch = new HashSet<Node>(Arrays.asList(nodeArray));
			
			Road r = Collections.min(graph.roadNodes.get(setToSearch), new Comparator<Road>() {

				@Override
				public int compare(Road r1, Road r2) {
					if (d.predictions.get(r1) > d.predictions.get(r2)) {
						return 1;
					}
					else if (d.predictions.get(r1) == d.predictions.get(r2)) {
						return 0;
					}
					return -1;
				}
				
			});
						
			roadPath.add(r);			
		}
		return roadPath;
	}
	
	private void findCheapestNeighbors(Day day) {
		
		for(Node n:  graph.nodes.values()) {
			for(Road r : n.neighbors.keySet()) {
				if(!n.cheapestRoads.containsKey(n.neighbors.get(r))) {
					n.cheapestRoads.put(n.neighbors.get(r), r);
				}else {
					Road existing = n.cheapestRoads.get(n.neighbors.get(r));
					
					if(day.predictions.get(existing) > day.predictions.get(r)){
						n.cheapestRoads.put(n.neighbors.get(r),r);
					}
				}
			}
		}
	}
	
	public void simulate() {
		
		PathFinder lrdastar = new LearingRealTimeAstar(graph);
		PathFinder idastar = new IterativeDeepeningAstar(graph, destination);
		
		for (Day d : days) {
			System.out.println("Day: " + d.ID);
			findCheapestNeighbors(d);
			graph.resetNodes();
			Result r1 = dijkstra(source, d);
			Result r2 = idastar.execute(source, destination, d);
			Result r3 = lrdastar.execute(source, destination, d);
			d.results.add(r1);
			d.results.add(r2);
			d.results.add(r3);
			
			for (Result r : d.results) {
				System.out.println(r);
			}
		}
	}
}
