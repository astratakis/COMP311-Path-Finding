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
		
		simulate(Algorithms.BREADTH_FIRST_SEARCH);
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
			ucs(days.get(0),graph,source,destination);
			break;
		case BREADTH_FIRST_SEARCH:
			bfs(graph,source,destination,days.get(0));
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
			System.out.println("-->Exploring :"+curNode.name);
			if(frontier.isEmpty()) {
				break;
			}
			
			curNode = frontier.pop();
			
			if(curNode.equals(destination)) {
				break;
			}
			
			explored.add(curNode);
					
			for(Road r : curNode.neighbors.keySet()) {
				
				Node child = curNode.neighbors.get(r);
				r.tmpPredictedWeight = d.predictions.get(r.name);
				r.tmpActualWeight = d.actual.get(r.name);
				
				if(!explored.contains(child) && !frontier.contains(child)) {		
					frontier.add(curNode.neighbors.get(r));
				}else if(frontier.contains(child)){
									
				}
			}
		}
		
		return path;
	}
	
	
	public LinkedList<Node> bfs(Graph graph, Node source, Node destination, Day d) {
		
		LinkedList<Node> queue = new LinkedList<Node>();
		LinkedList<Node> path = new LinkedList<Node>();
		HashSet<Node> explored = new HashSet<Node>();
		LinkedList<Node> spt = new LinkedList<Node>();
		
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
			}
		}
		
		for(Node n : path) {
			System.out.println(n.name);
		}
		
		Node currentSrc = destination;
		spt.add(destination);
		
		while(!path.isEmpty()) {
			curNode = path.get(path.size()-1);
			path.remove(curNode);
			
			if(currentSrc.neighbors.containsValue(curNode)) {
				spt.add(curNode);
				currentSrc = curNode;
				
				if(curNode.equals(source)){
					break;
				}
			}
			
		}
		
		System.out.println("------------------------SPT---------------------------");
		
		for(Node n : spt) {
			System.out.println(n.name);
		}
		
	
		System.out.println("-------------------------SPT ROADS--------------------------");

		LinkedList<Road> roadPath = findRoadPath(spt);
		
		for(Road r: roadPath) {
			System.out.println(r);
		}
				
		return spt;		
	}
	
	public LinkedList<Road> findRoadPath(LinkedList<Node> nodePath){
		
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
