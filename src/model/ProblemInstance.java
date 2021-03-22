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
	
	public void simulate() {
		
		for (int i=0; i<days.size(); i++) {
			
			System.out.println("Day: " + (i+1));
			
			// Run bfs with predicions ...
			
			bfs(graph, source, destination, days.get(i));
			
			// Run ida* with predicions ...
			
			ida(graph, source, destination, days.get(i));
		}
		
	}
	
	private LinkedList<Node> ida(Graph graph, Node source, Node destination, Day d) {
		return null;
	}
	
	private LinkedList<Node> bfs(Graph graph, Node source, Node destination, Day d) {
		
		long start = System.nanoTime();
		
		LinkedList<Node> queue = new LinkedList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		LinkedList<Node> spt = new LinkedList<Node>();
		
		int visitedNodes = 0;
		
		path.add(source);
		queue.add(source);
		explored.add(source);
		
		Node curNode;
		
		while(!queue.isEmpty()) {
			curNode = queue.pop();
		
			for(Node n: curNode.neighbors.values()) {
				
				if(!explored.contains(n)){
					queue.add(n);
					explored.add(n);
					path.add(n);
					
					if(curNode.equals(destination)) {	
						break;
					}
				}
				
				visitedNodes++;
			}
		}
		
		Node currentSrc = destination;
		spt.add(destination);
		
		while(!path.isEmpty()) {
			curNode = path.get(path.size()-1);
			path.remove(curNode);
			
			if(currentSrc.neighbors.containsValue(curNode)) {
				spt.add(curNode);
				currentSrc = curNode;
				
				if(curNode.equals(source)) {
					break;
				}
			}
			
		}
		
		LinkedList<Road> roadPath = findRoadPath(spt);
		double totCost = 0.0;
		double realCost = 0.0;
		
		long end = System.nanoTime();
		
		Collections.reverse(roadPath);
		
		for(Road r: roadPath) {
			totCost += d.predictions.get(r.name);
			realCost += d.actual.get(r.name);
		}
						
		System.out.println("<BREADTH FIRST SEARCH>: ");
		System.out.println("\tVisited Nodes Num: " + visitedNodes);
		System.out.println("\tExecution time: " + (end - start < 1_000_000 ? end - start + " ns" : (end - start) / 1_000_000 + " ms"));
		
		StringBuilder buffer = new StringBuilder();
		
		for (int i=0; i<roadPath.size(); i++) {
			buffer.append(roadPath.get(i).name + " ( " + d.predictions.get(roadPath.get(i).name) + " )");
			if (i < roadPath.size() - 1) {
				buffer.append(" -> ");
			}
		}
		
		System.out.println("\tPath: " + buffer.toString());
		System.out.printf("\tPredicted Cost: %.2f\n", totCost);
		System.out.printf("\tReal Cost: %.2f\n\n", realCost);
				
		return spt;
	}
	
	private LinkedList<Road> findRoadPath(LinkedList<Node> nodePath){
		
		LinkedList<Road> roadPath = new LinkedList<Road>();
		
		for(int i=0; i<nodePath.size(); i++) {
			
			if(nodePath.isEmpty() || i+1 >= nodePath.size()) {
				break;
			}
			
			Node n1 = nodePath.get(i);
			Node n2 = nodePath.get(i+1);
			
			Node[] nodeArray = {n1,n2};
			HashSet<Node> setToSearch = new HashSet<Node>(Arrays.asList(nodeArray));
			
			Road r = graph.roadNodes.get(setToSearch);
			
			roadPath.add(r);			
		}
		
		return roadPath;
	}
	
	public void export() throws IOException {
		PrintWriter pr = new PrintWriter(new File("output" + File.separator + "output.txt"));
	}
}
