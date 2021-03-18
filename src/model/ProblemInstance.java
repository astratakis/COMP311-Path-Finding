package model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
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
		
		graph = new Graph(scanner);
		
		this.source = graph.nodes.get(source);
		this.destination = graph.nodes.get(destination);
		
		days = new ArrayList<Day>();
		
		int dayID = 0;
		
		while (true) {
			
			try {
				Day d = new Day(dayID++, graph, scanner);
				days.add(d);
			}
			catch (Exception e) {
				break;
			}
		}
		
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
				
				days.get(i).actual.put(arr[0], virtualWeight);
			}
		}
		
		scanner.close();
	}
	
	final Node source;
	final Node destination;
	final Graph graph;
	
	final List<Day> days;
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("======== < DESCRIPTION > ========\n\n");
		buffer.append("Source: " + source + "\n");
		buffer.append("Destination: " + destination + "\n");
		buffer.append("\n=================================\n\n");
		
		buffer.append(graph);
		return buffer.toString();
	}
	
	public void simulate(Algorithms alg) {
		switch (alg) {
		
		case DIJKSTRA:
			dijkstra(graph, source, destination);
			break;
		case UNIFORM_COST_SEARCH:
			break;
		case BREADTH_FIRST_SEARCH:
			break;
		
			
		default: 
			break;
		}
	}
	
	/**
	 * Shortest Path Search Algorithm (Dijkstra)
	 * @param g
	 * @param source
	 */
	public void dijkstra(Graph g, Node source, Node destination) {
		
		double totalDistranceTraveled = 0.0;
		Node curNode = source;
	}
	
	public LinkedList<Road> ucs(Day d, Graph g, Node source, Node destination) {
		
		double totalCost = 0.0;
		Node curNode = source;
		
		LinkedList<Node> frontier = new LinkedList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		LinkedList<Road> path = new LinkedList<Road>();
		
		frontier.add(curNode);
		
		while(true) {
			
			if(frontier.isEmpty()) {
				return null;
			}
			
			curNode = frontier.pop();
			
			if(curNode.equals(destination)) {
				return path;
			}
			
			explored.add(curNode);
					
			for(Road r : curNode.neighbors.keySet()) {
				if(!explored.contains(curNode.neighbors.get(r))) {
					
					frontier.add(curNode.neighbors.get(r));
				}
			}
		}
	}
	
	
	public void bfs() {
		
	}
	
	public void export() throws IOException {
		PrintWriter pr = new PrintWriter(new File("output" + File.separator + "output.txt"));
	}
}
