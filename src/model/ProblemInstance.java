package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
		
		graph = new Graph(nodes, roads);
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
	
	public void simulate() {
		
		PathFinder lrdastar = new LearingRealTimeAstar(graph);
		
		for (Day d : days) {
			Result r = lrdastar.execute(source, destination, d);
			System.out.println(r);
			d.results.add(r);
		}
	}
}
